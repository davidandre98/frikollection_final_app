namespace Frikollection_Api.DTOs.Collection
{
    public class CreateCollectionDto
    {
        public string Name { get; set; }
        public bool Private { get; set; } = false;
        public Guid UserId { get; set; }  // A quin usuari pertany
    }
}
