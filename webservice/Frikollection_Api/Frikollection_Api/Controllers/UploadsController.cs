using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

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

        [HttpPost("{type}")]
        public async Task<IActionResult> UploadImage([FromRoute] string type, IFormFile file)
        {
            if (file == null || file.Length == 0)
                return BadRequest(new { message = "No s'ha proporcionat cap fitxer." });

            var allowedTypes = new[] { "avatar", "product", "tag" };
            if (!allowedTypes.Contains(type.ToLower()))
                return BadRequest(new { message = $"Tipus d'imatge no vàlid. Usa: {string.Join(", ", allowedTypes)}" });

            var uploadsFolder = Path.Combine(_env.WebRootPath ?? "wwwroot", "uploads", type);
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

            var imageUrl = $"{Request.Scheme}://{Request.Host}/images/uploads/{type}/{originalFileName}";
            return Ok(new { url = imageUrl });
        }
    }
}
