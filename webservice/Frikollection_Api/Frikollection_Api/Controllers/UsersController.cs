using Frikollection_Api.DTOs.Collection;
using Frikollection_Api.DTOs.Notification;
using Frikollection_Api.DTOs.User;
using Frikollection_Api.Models;
using Frikollection_Api.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace Frikollection_Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UsersController : ControllerBase
    {
        private readonly IUserService _userService;
        private readonly ICollectionService _collectionService;

        public UsersController(IUserService userService, ICollectionService collectionService)
        {
            _userService = userService;
            _collectionService = collectionService;
        }

        // POST: api/users
        [HttpPost]
        public async Task<ActionResult<User>> Register([FromBody] RegisterUserDto dto)
        {
            try
            {
                var user = await _userService.RegisterAsync(dto);
                return CreatedAtAction(nameof(GetById), new { id = user.UserId }, user);
            }
            catch (InvalidOperationException ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }

        // GET: api/users/{id}
        [HttpGet("{id}")]
        public async Task<ActionResult<User>> GetById(Guid id)
        {
            var user = await _userService.GetByIdAsync(id);
            if (user == null)
                return NotFound();

            return Ok(user);
        }

        // PUT: api/users/{id}
        [HttpPut("{id}")]
        public async Task<ActionResult<User>> Update(Guid id, [FromBody] UpdateUserDto dto)
        {
            try
            {
                var updatedUser = await _userService.UpdateUserAsync(id, dto);
                if (updatedUser == null)
                    return NotFound(new { error = "Usuari no trobat." });

                return Ok(updatedUser);
            }
            catch (InvalidOperationException ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }

        // GET: api/users/{id}/public
        [HttpGet("{id}/public")]
        public async Task<ActionResult<PublicUserDto>> GetPublicProfile(Guid id)
        {
            var user = await _userService.GetPublicProfileAsync(id);
            if (user == null)
                return NotFound(new { error = "Usuari no trobat." });

            return Ok(user);
        }

        // GET: api/users/{id}/profile
        [HttpGet("{id}/profile")]
        public async Task<ActionResult<UserProfileDto>> GetOwnProfile(Guid id)
        {
            var user = await _userService.GetUserProfileAsync(id);
            if (user == null)
                return NotFound(new { error = "Usuari no trobat." });

            return Ok(user);
        }

        // GET: api/users/{id}/collections  -> Retorna totes les Collection
        // GET: api/users/{id}/collections?visibility=public  -> Retorna totes les Collection que són públiques
        // GET: api/users/{id}/collections?visibility=private  -> Retorna totes les Collection que són privades
        [HttpGet("{id}/collections")]
        public async Task<ActionResult<IEnumerable<UserCollectionDto>>> GetUserCollections(Guid id, [FromQuery] string? visibility)
        {
            var collections = await _collectionService.GetUserCollectionsAsync(id, visibility);

            if (!collections.Any())
                return NotFound(new { message = "Aquest usuari no té col·leccions amb aquest filtre." });

            return Ok(collections);
        }

        // GET: api/users/{id}/collections/public
        [HttpGet("{id}/collections/public")]
        public async Task<ActionResult<IEnumerable<UserCollectionDto>>> GetPublicCollections(Guid id)
        {
            var collections = await _collectionService.GetPublicCollectionsByUserAsync(id);

            if (!collections.Any())
                return NotFound(new { message = "Aquest usuari no té col·leccions públiques." });

            return Ok(collections);
        }

        // DELETE: /api/users/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteUser(Guid id)
        {
            var result = await _userService.DeleteUserAsync(id);
            if (!result)
                return NotFound(new { message = "Usuari no trobat." });

            return NoContent();
        }

        // GET: api/users/{id}/notifications
        [HttpGet("{id}/notifications")]
        public async Task<ActionResult<IEnumerable<NotificationDto>>> GetNotifications(Guid id)
        {
            var notifications = await _userService.GetNotificationsAsync(id);

            return Ok(notifications);
        }

        // GET: api/users/{id}/notifications/unread-count
        [HttpGet("{id}/notifications/unread-count")]
        public async Task<ActionResult<int>> GetUnreadNotificationCount(Guid id)
        {
            var count = await _userService.GetUnreadNotificationCountAsync(id);
            return Ok(count);
        }

        // DELETE: api/users/{id}/notifications/{id}
        [HttpDelete("{userId}/notifications/{notificationId}")]
        public async Task<IActionResult> DeleteNotification(Guid userId, Guid notificationId)
        {
            var success = await _userService.DeleteNotificationAsync(notificationId, userId);
            if (!success)
                return NotFound(new { message = "Notificació no trobada o no tens permisos per eliminar-la." });

            return Ok(new { message = "Notificació eliminada." });
        }

        // DELETE: api/users/{id}/notifications
        [HttpDelete("{id}/notifications")]
        public async Task<IActionResult> DeleteAllNotifications(Guid id)
        {
            var deleted = await _userService.DeleteAllNotificationsAsync(id);
            return Ok(new { message = $"S'han eliminat {deleted} notificacions." });
        }
    }
}
