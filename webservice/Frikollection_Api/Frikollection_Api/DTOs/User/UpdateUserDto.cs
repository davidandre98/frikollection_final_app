using System.ComponentModel.DataAnnotations;

namespace Frikollection_Api.DTOs.User
{
    public class UpdateUserDto
    {
        public string? Avatar { get; set; }

        [StringLength(16, MinimumLength = 2)]
        public string? Nickname { get; set; }

        public string? FirstName { get; set; }

        public string? LastName { get; set; }

        [Phone]
        public string? PhoneNumber { get; set; }

        public DateOnly? Birthday { get; set; }

        [StringLength(200)]
        public string? Biography { get; set; }

        [MinLength(3)]
        public string? Username { get; set; }

        // Canvi de contrasenya
        public string? CurrentPassword { get; set; }

        [MinLength(6)]
        public string? NewPassword { get; set; }
    }
}
