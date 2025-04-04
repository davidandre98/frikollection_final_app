namespace Frikollection_Api.DTOs.Collection
{
    public class CollectionDto
    {
        public Guid CollectionId { get; set; }
        public string Name { get; set; }
        public bool Private { get; set; }
        public DateOnly CreationDate { get; set; }
        public int TotalProducts { get; set; }
    }
}
