using Frikollection_Api.DTOs.Tag;
using Frikollection_Api.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace Frikollection_Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class TagsController : ControllerBase
    {
        private readonly ITagService _service;

        public TagsController(ITagService service)
        {
            _service = service;
        }

        // POST: api/tags
        [HttpPost]
        public async Task<IActionResult> Create([FromBody] CreateTagDto dto)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            var created = await _service.CreateAsync(dto);
            return CreatedAtAction(nameof(GetById), new { id = created.TagId }, created);
        }

        // GET: api/tags
        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var tags = await _service.GetAllAsync();
            var dtoResult = tags.Select(_service.ToDto);
            return Ok(dtoResult);
        }

        // GET: api/tags/{id}
        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(Guid id)
        {
            var tag = await _service.GetByIdAsync(id);
            if (tag == null)
                return NotFound(new { message = "Etiqueta no trobada." });

            var dto = _service.ToDto(tag);
            return Ok(dto);
        }

        // PUT: api/tags/{id}
        [HttpPut("{id}")]
        public async Task<IActionResult> Update(Guid id, [FromBody] UpdateTagDto dto)
        {
            if (id != dto.TagId)
                return BadRequest(new { message = "L'ID no coincideix amb el cos de la petició." });

            var updated = await _service.UpdateAsync(dto);
            if (!updated)
                return NotFound(new { message = "Etiqueta no trobada." });

            return Ok(new { message = "Etiqueta actualitzada correctament." });
        }

        // DELETE: api/tags/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(Guid id)
        {
            var deleted = await _service.DeleteAsync(id);
            if (!deleted)
                return BadRequest(new { message = "No s'ha pogut eliminar. Potser està en ús o no existeix." });

            return Ok(new { message = "Etiqueta eliminada correctament." });
        }
    }
}
