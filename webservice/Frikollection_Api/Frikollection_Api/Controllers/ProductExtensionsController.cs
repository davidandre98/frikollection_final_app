using Frikollection_Api.DTOs.ProductExtension;
using Frikollection_Api.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace Frikollection_Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProductExtensionsController : ControllerBase
    {
        private readonly IProductExtensionService _service;

        public ProductExtensionsController(IProductExtensionService service)
        {
            _service = service;
        }

        // POST: api/productextensions
        [HttpPost]
        public async Task<IActionResult> Create([FromBody] CreateProductExtensionDto dto)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            var created = await _service.CreateAsync(dto);
            return CreatedAtAction(nameof(GetById), new { id = created.ProductExtensionId }, created);
        }

        // GET: api/productextensions
        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var extensions = await _service.GetAllAsync();
            return Ok(extensions);
        }

        // GET: api/productextensions/{id}
        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(Guid id)
        {
            var extension = await _service.GetByIdAsync(id);
            if (extension == null)
                return NotFound(new { message = "Extensió no trobada." });

            return Ok(extension);
        }

        // PUT: api/productextensions/{id}
        [HttpPut("{id}")]
        public async Task<IActionResult> Update(Guid id, [FromBody] UpdateProductExtensionDto dto)
        {
            if (id != dto.ProductExtensionId)
                return BadRequest(new { message = "L'ID no coincideix amb el cos de la petició." });

            var updated = await _service.UpdateAsync(dto);
            if (!updated)
                return NotFound(new { message = "Extensió no trobada." });

            return Ok(new { message = "Extensió actualitzada correctament." });
        }

        // DELETE: api/productextensions/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(Guid id)
        {
            var deleted = await _service.DeleteAsync(id);
            if (!deleted)
                return BadRequest(new { message = "No s'ha pogut eliminar. Potser l'extensió està en ús o no existeix." });

            return Ok(new { message = "Extensió eliminada correctament." });
        }
    }
}
