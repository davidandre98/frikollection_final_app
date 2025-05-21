using Frikollection_Api.DTOs.Product;

namespace Frikollection_Api.DTOs.Collection
{
    public class UserCollectionDto
    {
        public Guid CollectionId { get; set; }

        public string Name { get; set; }

        public bool? Private { get; set; }

        public DateOnly? CreationDate { get; set; }

        public ICollection<ProductDto>? Products { get; set; } = new List<ProductDto>();
    }
}
