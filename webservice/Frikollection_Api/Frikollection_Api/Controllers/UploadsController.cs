using Frikollection_Api.DTOs.User;
using Frikollection_Api.Infraestructure;
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
        private readonly FrikollectionContext _context;

        public UploadsController(IWebHostEnvironment env, FrikollectionContext context)
        {
            _env = env;
            _context = context;
        }

        // Qualsevol imatge excepte l'avatar de l'usuari
        // POST: api/uploads/images/{productType}/{fileName}
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

        // Per a gestionar els avatars
        // POST: api/uploads/images/avatar/{userId}
        [HttpPost("avatar/{userId}")]
        public async Task<IActionResult> UploadAvatar([FromRoute] Guid userId, [FromForm] UploadAvatarDto dto)
        {
            var file = dto.File;

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

        // Agafar totes les imatges
        // GET: api/uploads/images
        [HttpGet("images")]
        public IActionResult GetAllProductImages()
        {
            var basePath = Path.Combine(_env.WebRootPath ?? "wwwroot", "uploads");
            var allowedTypes = new[] { "avatar", "figure", "funko", "tag", "tcg" };

            var imageUrls = new List<object>();

            foreach (var type in allowedTypes)
            {
                var typeFolder = Path.Combine(basePath, type);
                if (!Directory.Exists(typeFolder))
                    continue;

                var images = Directory.GetFiles(typeFolder)
                    .Select(file => $"{Request.Scheme}://{Request.Host}/images/uploads/{type}/{Path.GetFileName(file)}")
                    .ToList();

                imageUrls.Add(new
                {
                    Type = type,
                    Images = images
                });
            }

            return Ok(imageUrls);
        }

        // Agafar les imatges d'un productType en concret
        // GET: api/uploads/images/{productType}
        [HttpGet("images/{productType}")]
        public IActionResult GetProductImagesByType(string productType)
        {
            var allowedTypes = new[] { "avatar", "figure", "funko", "tag", "tcg" };
            if (!allowedTypes.Contains(productType.ToLower()))
                return BadRequest(new { message = $"Tipus d'imatge no vàlid. Usa: {string.Join(", ", allowedTypes)}" });

            var typeFolder = Path.Combine(_env.WebRootPath ?? "wwwroot", "uploads", productType);
            if (!Directory.Exists(typeFolder))
                return NotFound(new { message = $"No s'ha trobat la carpeta per al tipus '{productType}'." });

            var imageUrls = Directory.GetFiles(typeFolder)
                .Select(file => $"{Request.Scheme}://{Request.Host}/images/uploads/{productType}/{Path.GetFileName(file)}")
                .ToList();

            return Ok(imageUrls);
        }

        // DELETE: api/uploads/delete-files
        [HttpDelete("delete-files")]
        public IActionResult DeleteTemporaryImages([FromQuery] string productType, [FromQuery] string[] fileNames)
        {
            var allowedTypes = new[] { "figure", "funko", "tag", "tcg" };
            if (!allowedTypes.Contains(productType.ToLower()))
            {
                return BadRequest(new { message = $"Tipus d'imatge no vàlid. Usa: {string.Join(", ", allowedTypes)}" });
            }

            var uploadFolder = Path.Combine(_env.WebRootPath ?? "wwwroot", "uploads", productType);
            var deletedFiles = new List<string>();
            var notFoundFiles = new List<string>();

            foreach (var name in fileNames)
            {
                var filePath = Path.Combine(uploadFolder, name);

                if (System.IO.File.Exists(filePath))
                {
                    System.IO.File.Delete(filePath);
                    deletedFiles.Add(name);
                }
                else
                {
                    notFoundFiles.Add(name);
                }
            }

            return Ok(new
            {
                ProductType = productType,
                Deleted = deletedFiles,
                NotFound = notFoundFiles
            });
        }

        // DELETE: api/uploads/delete-all
        [HttpDelete("delete-all")]
        public IActionResult DeleteAllImagesOfProductType([FromQuery] string productType)
        {
            var allowedTypes = new[] { "figure", "funko", "tag", "tcg" };
            if (!allowedTypes.Contains(productType.ToLower()))
            {
                return BadRequest(new { message = $"Tipus d'imatge no vàlid. Usa: {string.Join(", ", allowedTypes)}" });
            }

            var uploadFolder = Path.Combine(_env.WebRootPath ?? "wwwroot", "uploads", productType);

            if (!Directory.Exists(uploadFolder))
            {
                return NotFound(new { message = $"La carpeta per al tipus '{productType}' no existeix." });
            }

            var deletedFiles = new List<string>();
            var failedToDelete = new List<string>();

            var files = Directory.GetFiles(uploadFolder);
            foreach (var file in files)
            {
                try
                {
                    System.IO.File.Delete(file);
                    deletedFiles.Add(Path.GetFileName(file));
                }
                catch
                {
                    failedToDelete.Add(Path.GetFileName(file));
                }
            }

            return Ok(new
            {
                ProductType = productType,
                Deleted = deletedFiles,
                Failed = failedToDelete
            });
        }
    }
}
