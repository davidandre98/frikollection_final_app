using System.ComponentModel.DataAnnotations;
using System.Text.RegularExpressions;

namespace Frikollection_Api.DTOs.User
{
    public class UpdateUserDto : IValidatableObject
    {
        public string? Avatar { get; set; }

        [StringLength(16, MinimumLength = 2)]
        public string? Nickname { get; set; }

        public string? FirstName { get; set; }

        public string? LastName { get; set; }

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

        public IEnumerable<ValidationResult> Validate(ValidationContext validationContext)
        {
            if (!string.IsNullOrWhiteSpace(PhoneNumber))
            {
                if (!Regex.IsMatch(PhoneNumber, @"^\d{9}$"))
                {
                    yield return new ValidationResult("El número de telèfon ha de tenir exactament 9 dígits.", new[] { nameof(PhoneNumber) });
                }
            }

            if (!string.IsNullOrWhiteSpace(NewPassword))
            {
                if (NewPassword.Length < 6)
                {
                    yield return new ValidationResult("La nova contrasenya ha de tenir com a mínim 6 caràcters.", new[] { nameof(NewPassword) });
                }

                if (!Regex.IsMatch(NewPassword, @"[A-Z]"))
                {
                    yield return new ValidationResult("La nova contrasenya ha de contenir almenys una majúscula.", new[] { nameof(NewPassword) });
                }

                if (!Regex.IsMatch(NewPassword, @"[a-z]"))
                {
                    yield return new ValidationResult("La nova contrasenya ha de contenir almenys una minúscula.", new[] { nameof(NewPassword) });
                }

                if (!Regex.IsMatch(NewPassword, @"\\d"))
                {
                    yield return new ValidationResult("La nova contrasenya ha de contenir almenys un número.", new[] { nameof(NewPassword) });
                }
            }
        }
    }
}
