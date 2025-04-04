using Frikollection_Api.DTOs.Notification;
using Frikollection_Api.DTOs.User;
using Frikollection_Api.Infraestructure;
using Frikollection_Api.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;

namespace Frikollection_Api.Services
{
    public class UserService : IUserService
    {
        private readonly FrikollectionContext _context;
        private readonly IPasswordHasher<User> _passwordHasher;

        public UserService(FrikollectionContext context, IPasswordHasher<User> passwordHasher)
        {
            _context = context;
            _passwordHasher = passwordHasher;
        }

        public async Task<User> RegisterAsync(RegisterUserDto dto)
        {
            if (await _context.Users.AnyAsync(u => u.Email == dto.Email))
                throw new InvalidOperationException("Ja existeix un usuari amb aquest correu.");

            if (await _context.Users.AnyAsync(u => u.Username == dto.Username))
                throw new InvalidOperationException("Ja existeix un usuari amb aquest nom d’usuari.");

            var user = new User
            {
                UserId = Guid.NewGuid(),
                Username = dto.Username,
                Email = dto.Email,
                RegisterDate = DateTime.UtcNow
            };

            // Hashejar la contrasenya abans de guardar-la
            user.Password = _passwordHasher.HashPassword(user, dto.Password);

            _context.Users.Add(user);
            await _context.SaveChangesAsync();

            return user;
        }

        public async Task<User?> GetByIdAsync(Guid id)
        {
            return await _context.Users.FindAsync(id);
        }

        public async Task<User?> LoginAsync(LoginDto dto)
        {
            var user = await _context.Users.FirstOrDefaultAsync(u =>
                u.Email == dto.Identifier || u.Username == dto.Identifier);

            if (user == null)
                return null;

            var result = _passwordHasher.VerifyHashedPassword(user, user.Password, dto.Password);

            if (result == PasswordVerificationResult.Failed)
                return null;

            return user;
        }

        public async Task<User?> UpdateUserAsync(Guid id, UpdateUserDto dto)
        {
            var user = await _context.Users.FindAsync(id);
            if (user == null)
                return null;

            if (!string.IsNullOrWhiteSpace(dto.Username) && dto.Username != user.Username)
            {
                var usernameExists = await _context.Users
                    .AnyAsync(u => u.Username == dto.Username && u.UserId != id);

                if (usernameExists)
                    throw new InvalidOperationException("Aquest nom d’usuari ja està en ús.");

                user.Username = dto.Username;
            }

            if (!string.IsNullOrWhiteSpace(dto.NewPassword))
            {
                if (string.IsNullOrWhiteSpace(dto.CurrentPassword))
                    throw new InvalidOperationException("Has d’introduir la contrasenya actual.");

                var result = _passwordHasher.VerifyHashedPassword(user, user.Password, dto.CurrentPassword);

                if (result == PasswordVerificationResult.Failed)
                    throw new InvalidOperationException("La contrasenya actual no és correcta.");

                user.Password = _passwordHasher.HashPassword(user, dto.NewPassword);
            }

            if (dto.Avatar != null) user.Avatar = dto.Avatar;
            if (dto.Nickname != null) user.Nickname = dto.Nickname;
            if (dto.FirstName != null) user.FirstName = dto.FirstName;
            if (dto.LastName != null) user.LastName = dto.LastName;
            if (dto.PhoneNumber != null) user.PhoneNumber = dto.PhoneNumber;
            if (dto.Birthday != null) user.Birthday = dto.Birthday;
            if (dto.Biography != null) user.Biography = dto.Biography;

            await _context.SaveChangesAsync();
            return user;
        }

        public async Task<PublicUserDto?> GetPublicProfileAsync(Guid id)
        {
            var user = await _context.Users.FindAsync(id);
            if (user == null)
                return null;

            return new PublicUserDto
            {
                Avatar = user.Avatar,
                Nickname = user.Nickname,
                FirstName = user.FirstName,
                LastName = user.LastName,
                Birthday = user.Birthday,
                Biography = user.Biography
            };
        }

        public async Task<UserProfileDto?> GetUserProfileAsync(Guid id)
        {
            var user = await _context.Users.FindAsync(id);
            if (user == null)
                return null;

            return new UserProfileDto
            {
                Username = user.Username,
                Email = user.Email,
                Avatar = user.Avatar,
                Nickname = user.Nickname,
                FirstName = user.FirstName,
                LastName = user.LastName,
                PhoneNumber = user.PhoneNumber,
                Birthday = user.Birthday,
                Biography = user.Biography,
                RegisterDate = user.RegisterDate,
                LastLogin = user.LastLogin
            };
        }

        public async Task<bool> DeleteUserAsync(Guid id)
        {
            var user = await _context.Users.FindAsync(id);
            if (user == null)
                return false;

            _context.Users.Remove(user);
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<IEnumerable<NotificationDto>> GetNotificationsAsync(Guid userId)
        {
            var notifications = await _context.Notifications
                .Where(n => n.RecipientUserId == userId)
                .Include(n => n.FollowerUser)
                .Include(n => n.Collection)
                .OrderByDescending(n => n.CreatedAt)
                .ToListAsync();

            // Marcar com a llegides les que no ho estaven
            var unreadNotifications = notifications.Where(n => !n.IsRead).ToList();
            foreach (var notif in unreadNotifications)
            {
                notif.IsRead = true;
            }

            if (unreadNotifications.Any())
                await _context.SaveChangesAsync();

            return notifications.Select(n => new NotificationDto
            {
                Message = n.Message,
                FollowerNickname = n.FollowerUser.Nickname,
                CollectionName = n.Collection.Name,
                CreatedAt = n.CreatedAt,
                IsRead = n.IsRead
            });
        }

        public async Task<int> GetUnreadNotificationCountAsync(Guid userId)
        {
            return await _context.Notifications
                .Where(n => n.RecipientUserId == userId && !n.IsRead)
                .CountAsync();
        }

        public async Task<bool> DeleteNotificationAsync(Guid notificationId, Guid userId)
        {
            var notification = await _context.Notifications
                .FirstOrDefaultAsync(n => n.NotificationId == notificationId && n.RecipientUserId == userId);

            if (notification == null)
                return false;

            _context.Notifications.Remove(notification);
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<int> DeleteAllNotificationsAsync(Guid userId)
        {
            var notifications = await _context.Notifications
                .Where(n => n.RecipientUserId == userId)
                .ToListAsync();

            if (!notifications.Any())
                return 0;

            _context.Notifications.RemoveRange(notifications);
            return await _context.SaveChangesAsync();
        }
    }
}
