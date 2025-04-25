using System.ComponentModel.DataAnnotations;

namespace Frikollection_Api.DTOs.Product
{
    public class CreateProductDto
    {
        [Required]
        [StringLength(100)]
        public string Name { get; set; } = null!;

        [StringLength(100)]
        public string? License { get; set; }

        [StringLength(50)]
        public string? Status { get; set; }

        [StringLength(100)]
        public string? Supertype { get; set; }

        [StringLength(100)]
        public string? Subtype { get; set; }

        public int? ItemNumber { get; set; }

        public decimal? Value { get; set; }
        public double? Width { get; set; }
        public double? Height { get; set; }
        public DateOnly? Release { get; set; }
        public string? SmallPictureUrl { get; set; }
        public string? BigPictureUrl { get; set; }
        public Guid? ProductTypeId { get; set; }
        public Guid? ProductExtensionId { get; set; }
        public List<Guid>? TagIds { get; set; }
    }
}
