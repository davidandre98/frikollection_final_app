using Frikollection_Api.DTOs.Collection;
using Frikollection_Api.DTOs.Notification;

namespace Frikollection_Api.DTOs.User
{
    public class UserDto
    {
        public string Username { get; set; }
        public string Avatar { get; set; }
        public string Nickname { get; set; }
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public string Email { get; set; }
        public string? PhoneNumber { get; set; }
        public DateOnly Birthday { get; set; }
        public string? Biography { get; set; }
        public DateTime? RegisterDate { get; set; }
        public DateTime? LastLogin { get; set; }
        public ICollection<CollectionDto> OwnCollections { get; set; } = new List<CollectionDto>();
        public ICollection<FollowedCollectionDto> FollowedCollections { get; set; } = new List<FollowedCollectionDto>();
        public ICollection<NotificationDto> Notifications { get; set; } = new List<NotificationDto>();
    }
}
