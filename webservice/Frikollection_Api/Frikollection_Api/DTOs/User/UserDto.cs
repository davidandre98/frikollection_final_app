using Frikollection_Api.DTOs.Collection;
using Frikollection_Api.DTOs.Notification;

namespace Frikollection_Api.DTOs.User
{
    public class UserDto
    {
        public Guid UserId { get; set; }
        public string Avatar { get; set; }
        public string Nickname { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public DateOnly? Birthday { get; set; }
        public string? Biography { get; set; }
        public ICollection<CollectionDto> OwnCollections { get; set; } = new List<CollectionDto>();
    }
}
