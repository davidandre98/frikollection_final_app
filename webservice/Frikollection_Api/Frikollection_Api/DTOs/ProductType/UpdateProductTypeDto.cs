using System.ComponentModel.DataAnnotations;

namespace Frikollection_Api.DTOs.ProductType
{
    public class UpdateProductTypeDto
    {
        [Required]
        public Guid ProductTypeId { get; set; }

        [Required]
        [StringLength(100)]
        public string TypeName { get; set; } = null!;

        public bool HasExtension { get; set; } = false;
    }
}
