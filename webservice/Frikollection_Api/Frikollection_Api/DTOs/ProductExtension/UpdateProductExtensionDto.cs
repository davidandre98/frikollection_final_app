using System.ComponentModel.DataAnnotations;

namespace Frikollection_Api.DTOs.ProductExtension
{
    public class UpdateProductExtensionDto
    {
        [Required]
        public Guid ProductExtensionId { get; set; }

        [Required]
        [StringLength(100)]
        public string Expansion { get; set; } = null!;

        [Required]
        [StringLength(100)]
        public string Package { get; set; } = null!;
    }
}
