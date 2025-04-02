using Frikollection_Api.DTOs.User;
using Frikollection_Api.Models;

namespace Frikollection_Api.Services
{
    public interface IUserService
    {
        Task<User> RegisterAsync(RegisterUserDto dto);
        Task<User?> GetByIdAsync(Guid id);
        Task<User?> UpdateUserAsync(Guid id, UpdateUserDto dto);
        Task<PublicUserDto?> GetPublicProfileAsync(Guid id);
        Task<UserProfileDto?> GetUserProfileAsync(Guid id);
        Task<bool> DeleteUserAsync(Guid id);
    }
}
