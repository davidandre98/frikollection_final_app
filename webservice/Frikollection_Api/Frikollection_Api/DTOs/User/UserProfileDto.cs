namespace Frikollection_Api.DTOs.User
{
    public class UserProfileDto
    {
        public Guid UserId { get; set; }
        public string Username { get; set; }
        public string Password { get; set; }

        public string Email { get; set; }

        public string? Avatar { get; set; }

        public string? Nickname { get; set; }

        public string? FirstName { get; set; }

        public string? LastName { get; set; }

        public string? PhoneNumber { get; set; }

        public DateOnly? Birthday { get; set; }

        public string? Biography { get; set; }

        public DateTime? RegisterDate { get; set; }

        public DateTime? LastLogin { get; set; }
    }
}
