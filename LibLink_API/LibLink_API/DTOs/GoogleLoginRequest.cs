namespace LibLink_API.DTOs
{
    public class GoogleLoginRequest
    {
        public string IdToken { get; set; } = string.Empty;

        // Required when registering a new account.
        public string? StudentNumber { get; set; }
    }
}