namespace Frikollection_Api.DTOs.Collection
{
    public class AddProductToCollectionDto
    {
        public Guid CollectionId { get; set; }
        public List<Guid> ProductIds { get; set; }
    }
}
