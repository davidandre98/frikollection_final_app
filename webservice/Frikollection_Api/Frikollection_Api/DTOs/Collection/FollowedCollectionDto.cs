namespace Frikollection_Api.DTOs.Collection
{
    public class FollowedCollectionDto
    {
        public Guid CollectionId { get; set; }
        public string Name { get; set; }
        public string OwnerNickname { get; set; }
        public DateTime FollowDate { get; set; }
    }
}
