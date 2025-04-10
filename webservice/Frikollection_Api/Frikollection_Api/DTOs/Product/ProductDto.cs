using Frikollection_Api.DTOs.ProductExtension;
using Frikollection_Api.DTOs.ProductType;

namespace Frikollection_Api.DTOs.Product
{
    public class ProductDto
    {
        public string Name { get; set; } = null!;
        public string? Supertype { get; set; }
        public string? Subtype { get; set; }
        public DateOnly? Release { get; set; }
        public string? Status { get; set; }
        public int? ItemNumber { get; set; }
        public string? License { get; set; }
        public double? Width { get; set; }
        public double? Height { get; set; }
        public decimal? Value { get; set; }
        public string? SmallPicture { get; set; }
        public string? BigPicture { get; set; }
        public ProductTypeDto? ProductType { get; set; }
        public ProductExtensionDto? ProductExtension { get; set; }
    }
}
