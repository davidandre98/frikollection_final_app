using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;

namespace Frikollection_Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class UploadsController : ControllerBase
    {
        private readonly IWebHostEnvironment _env;

        public UploadsController(IWebHostEnvironment env)
        {
            _env = env;
        }

        // Qualsevol imatge excepte l'avatar de l'usuari
        [HttpPost("{productType}")]
        public async Task<IActionResult> UploadImage([FromRoute] string productType, IFormFile file)
        {
            if (file == null || file.Length == 0)
                return BadRequest(new { message = "No s'ha proporcionat cap fitxer." });

            var allowedTypes = new[] { "figure", "funko", "tag", "tcg" };
            if (!allowedTypes.Contains(productType.ToLower()))
                return BadRequest(new { message = $"Tipus d'imatge no vàlid. Usa: {string.Join(", ", allowedTypes)}" });

            var uploadsFolder = Path.Combine(_env.WebRootPath ?? "wwwroot", "uploads", productType);
            if (!Directory.Exists(uploadsFolder))
                Directory.CreateDirectory(uploadsFolder);

            // Netejar el nom del fitxer
            var originalFileName = Path.GetFileName(file.FileName).Replace(" ", "_");
            var filePath = Path.Combine(uploadsFolder, originalFileName);

            // Comprovar si ja existeix
            if (System.IO.File.Exists(filePath))
            {
                return Conflict(new { message = $"Ja existeix un fitxer amb el nom '{originalFileName}'." });
            }

            using (var stream = new FileStream(filePath, FileMode.Create))
            {
                await file.CopyToAsync(stream);
            }

            var imageUrl = $"{Request.Scheme}://{Request.Host}/images/uploads/{productType}/{originalFileName}";
            return Ok(new { url = imageUrl });
        }

        /*
        // Per a gestionar els avatars
        [HttpPost("avatar/{userId}")]
        public async Task<IActionResult> UploadAvatar([FromRoute] Guid userId, [FromForm] IFormFile file)
        {
            if (file == null || file.Length == 0)
                return BadRequest(new { message = "No s'ha proporcionat cap fitxer." });

            var user = await _context.Users.FindAsync(userId);
            if (user == null)
                return NotFound(new { message = "Usuari no trobat." });

            var avatarFolder = Path.Combine(_env.WebRootPath ?? "wwwroot", "uploads", "avatar");
            if (!Directory.Exists(avatarFolder))
                Directory.CreateDirectory(avatarFolder);

            var fileExtension = Path.GetExtension(file.FileName);
            var fileName = $"{userId}{fileExtension}";
            var filePath = Path.Combine(avatarFolder, fileName);

            // 🧹 Esborrar imatge antiga si no és la per defecte
            if (!string.IsNullOrEmpty(user.Avatar) &&
                !user.Avatar.EndsWith("default.jpg", StringComparison.OrdinalIgnoreCase))
            {
                var oldFileName = Path.GetFileName(new Uri(user.Avatar).AbsolutePath);
                var oldFilePath = Path.Combine(avatarFolder, oldFileName);
                if (System.IO.File.Exists(oldFilePath))
                    System.IO.File.Delete(oldFilePath);
            }

            using var stream = new FileStream(filePath, FileMode.Create);
            await file.CopyToAsync(stream);

            var avatarUrl = $"{Request.Scheme}://{Request.Host}/images/uploads/avatar/{fileName}";
            user.Avatar = avatarUrl;
            await _context.SaveChangesAsync();

            return Ok(new { url = avatarUrl });
        }
        */
    }
}
