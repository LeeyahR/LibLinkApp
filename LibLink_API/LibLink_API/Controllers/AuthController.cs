using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Security.Cryptography;
using System.Text;

using Google.Apis.Auth;
using LibLink_API.DTOs;
using LibLink_API.Models;

using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;

namespace LibLink_API.Controllers
{
    [ApiController]
    [Route("api/auth")]
    public class AuthController : ControllerBase
    {
        private readonly LibLink_APIContext _context;
        private readonly IConfiguration _configuration;

        public AuthController(LibLink_APIContext context, IConfiguration configuration)
        {
            _context = context;
            _configuration = configuration;
        }

        [HttpPost("register")]
        public async Task<IActionResult> Register(RegisterRequest request)
        {
            if (string.IsNullOrWhiteSpace(request.FullName) ||
                string.IsNullOrWhiteSpace(request.Email) ||
                string.IsNullOrWhiteSpace(request.StudentNumber) ||
                string.IsNullOrWhiteSpace(request.Password))
            {
                return BadRequest("All fields are required.");
            }

            var email = request.Email.Trim().ToLowerInvariant();
            var studentNumber = request.StudentNumber.Trim();

            if (await _context.User.AnyAsync(x => x.Email == email))
            {
                return BadRequest("An account with this email already exists.");
            }

            if (await _context.User.AnyAsync(x => x.StudentNumber == studentNumber))
            {
                return BadRequest("This student number is already registered.");
            }

            var user = new User
            {
                FullName = request.FullName.Trim(),
                Email = email,
                StudentNumber = studentNumber,
                PasswordHash = BCrypt.Net.BCrypt.HashPassword(request.Password),
                Role = "Student"
            };

            _context.User.Add(user);
            await _context.SaveChangesAsync();

            return Ok(new
            {
                message = "Registration successful.",
                userId = user.UserID,
                token = GenerateJwtToken(user),
                name = user.FullName,
                email = user.Email,
                role = user.Role
            });
        }

        [HttpPost("login")]
        public async Task<IActionResult> Login(LoginRequest request)
        {
            var email = request.Email.Trim().ToLowerInvariant();

            var user = await _context.User.FirstOrDefaultAsync(x => x.Email == email);

            if (user == null || !BCrypt.Net.BCrypt.Verify(request.Password, user.PasswordHash))
            {
                return Unauthorized("Invalid email or password.");
            }

            return Ok(new
            {
                message = "Login successful.",
                userId = user.UserID,
                name = user.FullName,
                email = user.Email,
                role = user.Role,
                token = GenerateJwtToken(user)
            });
        }

        // -------------------------------
        // GOOGLE SSO
        // -------------------------------

        [HttpPost("google")]
        public async Task<IActionResult> GoogleLogin(GoogleLoginRequest request)
        {
            if (string.IsNullOrWhiteSpace(request.IdToken))
            {
                return BadRequest("Google ID token is required.");
            }

            var googleClientId = _configuration["Google:ClientId"];

            if (string.IsNullOrWhiteSpace(googleClientId))
            {
                return StatusCode(500, "Google authentication is not configured.");
            }

            GoogleJsonWebSignature.Payload payload;

            try
            {
                // Validate Google's ID token and its audience.
                payload = await GoogleJsonWebSignature.ValidateAsync(request.IdToken,
                    new GoogleJsonWebSignature.ValidationSettings
                    {
                        Audience = new[] { googleClientId }
                    });
            }
            catch (InvalidJwtException)
            {
                return Unauthorized("Invalid or expired Google authentication token.");
            }
            catch (Exception)
            {
                return Unauthorized("Google authentication could not be verified.");
            }

            // Only accept verified Google email addresses.
            if (payload.EmailVerified != true || string.IsNullOrWhiteSpace(payload.Email) || string.IsNullOrWhiteSpace(payload.Subject))
            {
                return Unauthorized("Your Google email must be verified.");
            }

            var email = payload.Email.Trim().ToLowerInvariant();
            var googleSubject = payload.Subject;

            // First check whether this Google account has already been linked.
            var user = await _context.User.FirstOrDefaultAsync(x => x.GoogleSubject == googleSubject);

            if (user == null)
            {
                // Check whether a LibLink account already exists with this verified email.
                user = await _context.User.FirstOrDefaultAsync(x => x.Email.ToLower() == email);

                if (user != null)
                {
                    // Prevent linking another Google identity to an already linked LibLink account.
                    if (!string.IsNullOrEmpty(user.GoogleSubject))
                    {
                        return Conflict("This LibLink account is linked to another Google account.");
                    }

                    // Link the verified Google identity to the existing LibLink account.
                    user.GoogleSubject = googleSubject;

                    await _context.SaveChangesAsync();
                }
                else
                {
                    // A new user must provide a student number.
                    if (string.IsNullOrWhiteSpace(request.StudentNumber))
                    {
                        return Ok(new
                        {
                            requiresRegistration = true,
                            message = "Please provide your student number to create a LibLink account.",
                            email = email,
                            name = payload.Name
                        });
                    }

                    var studentNumber = request.StudentNumber.Trim();

                    if (await _context.User.AnyAsync(x => x.StudentNumber == studentNumber))
                    {
                        return Conflict("This student number is already registered.");
                    }

                    var randomPassword = Convert.ToBase64String(RandomNumberGenerator.GetBytes(32));

                    user = new User
                    {
                        FullName = string.IsNullOrWhiteSpace(payload.Name)? email: payload.Name,

                        Email = email,
                        StudentNumber = studentNumber,
                        PasswordHash = BCrypt.Net.BCrypt.HashPassword(randomPassword),
                        GoogleSubject = googleSubject,
                        Role = "Student",
                        CreatedDate = DateTime.UtcNow
                    };

                    _context.User.Add(user);
                    await _context.SaveChangesAsync();
                }
            }

            // Issue a LibLink JWT after successful authentication.
            return Ok(new
            {
                message = "Google login successful.",
                requiresRegistration = false,
                userId = user.UserID,
                name = user.FullName,
                email = user.Email,
                role = user.Role,
                token = GenerateJwtToken(user)
            });
        }

        // -------------------------------
        // JWT GENERATION
        // -------------------------------

        private string GenerateJwtToken(User user)
        {
            var key = _configuration["Jwt:Key"] ?? throw new InvalidOperationException("JWT signing key is not configured.");

            var issuer = _configuration["Jwt:Issuer"] ?? throw new InvalidOperationException("JWT issuer is not configured.");

            var audience = _configuration["Jwt:Audience"] ?? throw new InvalidOperationException("JWT audience is not configured.");

            if (Encoding.UTF8.GetByteCount(key) < 32)
            {
                throw new InvalidOperationException("JWT signing key must contain at least 32 bytes.");
            }

            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(key));

            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

            var claims = new[]
            {
                new Claim(
                    ClaimTypes.NameIdentifier,
                    user.UserID.ToString()),

                new Claim(
                    ClaimTypes.Name,
                    user.FullName),

                new Claim(
                    ClaimTypes.Email,
                    user.Email),

                new Claim(
                    ClaimTypes.Role,
                    user.Role)
            };

            var token = new JwtSecurityToken(
                issuer: issuer,
                audience: audience,
                claims: claims,
                expires: DateTime.UtcNow.AddHours(2),
                signingCredentials: credentials
            );

            return new JwtSecurityTokenHandler()
                .WriteToken(token);
        }
    }
}
