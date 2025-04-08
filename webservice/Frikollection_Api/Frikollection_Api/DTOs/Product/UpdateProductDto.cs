using System.ComponentModel.DataAnnotations;

namespace Frikollection_Api.DTOs.Product
{
    public class UpdateProductDto
    {
        [Required]
        public Guid ProductId { get; set; }

        [Required]
        [StringLength(100)]
        public string Name { get; set; } = null!;

        public string? License { get; set; }
        public string? Status { get; set; }
        public string? Type { get; set; }
        public string? Subtype { get; set; }
        public string? ItemNumber { get; set; }
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
