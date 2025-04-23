using Frikollection_Api.DTOs.ProductType;
using Frikollection_Api.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace Frikollection_Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProductTypesController : ControllerBase
    {
        private readonly IProductTypeService _service;

        public ProductTypesController(IProductTypeService service)
        {
            _service = service;
        }

        // POST: api/producttypes
        [HttpPost]
        public async Task<IActionResult> Create([FromBody] CreateProductTypeDto dto)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            var created = await _service.CreateAsync(dto);
            var dtoResult = _service.ToDto(created);

            return Ok(dtoResult);
        }

        // GET: api/producttypes
        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var types = await _service.GetAllAsync();
            var dtoList = types.Select(t => _service.ToDto(t)).ToList();

            return Ok(dtoList);
        }

        // GET: api/producttypes/{id}
        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(Guid id)
        {
            var type = await _service.GetByIdAsync(id);
            if (type == null)
                return NotFound(new { message = "Tipus de producte no trobat." });

            var dto = _service.ToDto(type);
            return Ok(dto);
        }

        // PUT: api/producttypes/{id}
        [HttpPut("{id}")]
        public async Task<IActionResult> Update(Guid id, [FromBody] UpdateProductTypeDto dto)
        {
            if (id != dto.ProductTypeId)
                return BadRequest(new { message = "L'ID no coincideix amb el cos de la petició." });

            var updated = await _service.UpdateAsync(dto);
            if (!updated)
                return NotFound(new { message = "Tipus de producte no trobat." });

            return Ok(new { message = "Tipus de producte actualitzat correctament." });
        }

        // DELETE: api/producttypes/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(Guid id)
        {
            var deleted = await _service.DeleteAsync(id);
            if (!deleted)
                return BadRequest(new { message = "No s'ha pogut eliminar. Potser està en ús o no existeix." });

            return Ok(new { message = "Tipus de producte eliminat correctament." });
        }
    }
}
