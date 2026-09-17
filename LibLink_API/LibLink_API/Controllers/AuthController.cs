using LibLink_API.Models;
using Microsoft.AspNetCore.Mvc;
using LibLink_API.DTOs;
using Microsoft.EntityFrameworkCore;


namespace LibLink_API.Controllers
{
    [ApiController]
    [Route("api/auth")]
    public class AuthController : ControllerBase
    {
        private readonly LibLink_APIContext _context;

        public AuthController(LibLink_APIContext context)
        {
            _context = context;
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

            if (await _context.User.AnyAsync(x => x.Email == request.Email))
            {
                return BadRequest("An account with this email already exists.");
            }

            var user = new User
            {
                FullName = request.FullName,
                Email = request.Email,
                StudentNumber = request.StudentNumber,
                PasswordHash = BCrypt.Net.BCrypt.HashPassword(
                    request.Password),
                Role = "Student"
            };

            _context.User.Add(user);

            await _context.SaveChangesAsync();

            return Ok(new
            {
                message = "Registration successful.",
                userId = user.UserID
            });
        }

        [HttpPost("login")]
        public async Task<IActionResult> Login(LoginRequest request)
        {
            var user = await _context.User
                .FirstOrDefaultAsync(x => x.Email == request.Email);

            if (user == null)
                return Unauthorized("Invalid email or password.");

            var validPassword =
                BCrypt.Net.BCrypt.Verify(
                    request.Password,
                    user.PasswordHash);

            if (!validPassword)
                return Unauthorized("Invalid email or password.");

            return Ok(new
            {
                message = "Login successful.",
                userId = user.UserID,
                name = user.FullName,
                email = user.Email,
                role = user.Role
            });
        }
    }
}
