using Frikollection_Api.DTOs.Product;

namespace Frikollection_Api.DTOs.Collection
{
    public class FollowedCollectionDto
    {
        public Guid CollectionId { get; set; }
        public string Name { get; set; }
        public Boolean? Private { get; set; }
        public string? OwnerNickname { get; set; }
        public DateTime? FollowDate { get; set; }
        public ICollection<ProductDto> Products { get; set; } = new List<ProductDto>();
    }
}
