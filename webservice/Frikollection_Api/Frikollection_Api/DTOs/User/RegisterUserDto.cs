using System.ComponentModel.DataAnnotations;
using System.Text.RegularExpressions;

namespace Frikollection_Api.DTOs.User
{
    public class RegisterUserDto : IValidatableObject
    {
        [Required]
        [MinLength(3)]
        public string Username { get; set; }

        [Required]
        [EmailAddress]
        public string Email { get; set; }

        [Required]
        public string Password { get; set; }

        [Required]
        [MinLength(2)]
        public string FirstName { get; set; }

        [Required]
        [MinLength(2)]
        public string LastName { get; set; }

        [Required]
        [DataType(DataType.Date)]
        public DateOnly Birthday { get; set; }

        // Validació personalitzada de la contrasenya
        public IEnumerable<ValidationResult> Validate(ValidationContext validationContext)
        {
            if (Password.Length < 6)
            {
                yield return new ValidationResult("La contrasenya ha de tenir com a mínim 6 caràcters.", new[] { nameof(Password) });
            }

            if (!Regex.IsMatch(Password, @"[A-Z]"))
            {
                yield return new ValidationResult("La contrasenya ha de contenir almenys una majúscula.", new[] { nameof(Password) });
            }

            if (!Regex.IsMatch(Password, @"[a-z]"))
            {
                yield return new ValidationResult("La contrasenya ha de contenir almenys una minúscula.", new[] { nameof(Password) });
            }

            if (!Regex.IsMatch(Password, @"\d"))
            {
                yield return new ValidationResult("La contrasenya ha de contenir almenys un número.", new[] { nameof(Password) });
            }
        }
    }
}
