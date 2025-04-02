namespace Frikollection_Api.DTOs.Collection
{
    public class UserCollectionDto
    {
        public string Name { get; set; }

        public bool? Private { get; set; }

        public DateOnly? CreationDate { get; set; }
    }
}
