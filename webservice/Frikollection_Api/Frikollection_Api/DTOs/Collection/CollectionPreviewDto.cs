using Frikollection_Api.DTOs.Product;
using Frikollection_Api.DTOs.User;

namespace Frikollection_Api.DTOs.Collection
{
    public class CollectionPreviewDto
    {
        public string Name { get; set; }
        public bool? Private { get; set; }
        public DateOnly? CreationDate { get; set; }
        public UserPreviewDto? User { get; set; }
        public ICollection<ProductDto>? Products { get; set; } = new List<ProductDto>();
    }
}
