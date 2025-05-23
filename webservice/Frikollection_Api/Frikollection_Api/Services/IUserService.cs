using Frikollection_Api.DTOs.Notification;
using Frikollection_Api.DTOs.User;
using Frikollection_Api.Models;
using Frikollection_Api.Utils;

namespace Frikollection_Api.Services
{
    public interface IUserService
    {
        Task<User> RegisterAsync(RegisterUserDto dto);
        Task<User?> LoginAsync(LoginDto dto);
        Task<IEnumerable<UserDto>> GetAllUsersAsync();
        Task<UserDto?> GetByIdAsync(Guid id);
        Task<ServiceResult<User>> UpdateUserAsync(Guid id, UpdateUserDto dto);
        Task<PublicUserDto?> GetPublicProfileAsync(Guid id);
        Task<UserProfileDto?> GetUserProfileAsync(Guid id);
        Task<bool> DeleteUserAsync(Guid id);
        Task<IEnumerable<NotificationDto>> GetNotificationsAsync(Guid userId);
        Task<int> GetUnreadNotificationCountAsync(Guid userId);
        Task<bool> DeleteNotificationAsync(Guid notificationId, Guid userId);
        Task<int> DeleteAllNotificationsAsync(Guid userId);

    }
}
