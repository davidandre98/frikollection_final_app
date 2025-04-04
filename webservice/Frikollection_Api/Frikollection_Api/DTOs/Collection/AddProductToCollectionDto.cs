namespace Frikollection_Api.DTOs.Collection
{
    public class AddProductToCollectionDto
    {
        public Guid CollectionId { get; set; }
        public Guid ProductId { get; set; }
    }
}
