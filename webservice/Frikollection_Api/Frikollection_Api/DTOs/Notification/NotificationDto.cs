namespace Frikollection_Api.DTOs.Notification
{
    public class NotificationDto
    {
        public Guid CollectionId { get; set; } = Guid.NewGuid();
        public string Message { get; set; } = string.Empty;
        public string FollowerNickname { get; set; } = string.Empty;
        public string CollectionName { get; set; } = string.Empty;
        public DateTime CreatedAt { get; set; }
        public bool IsRead { get; set; }
    }
}
