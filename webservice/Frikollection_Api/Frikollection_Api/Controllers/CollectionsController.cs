using Frikollection_Api.DTOs.Collection;
using Frikollection_Api.Models;
using Frikollection_Api.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace Frikollection_Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CollectionsController : ControllerBase
    {
        private readonly ICollectionService _collectionService;

        public CollectionsController(ICollectionService collectionService)
        {
            _collectionService = collectionService;
        }

        // POST: api/collections
        [HttpPost]
        public async Task<ActionResult<Collection>> Create([FromBody] CreateCollectionDto dto)
        {
            try
            {
                var created = await _collectionService.CreateCollectionAsync(dto);
                return CreatedAtAction(nameof(GetById), new { id = created.CollectionId }, created);
            }
            catch (InvalidOperationException ex)
            {
                // Retorna error 400 amb missatge llegible
                return BadRequest(new { error = ex.Message });
            }
            catch (Exception)
            {
                // Retorna error 500 genèric si passa qualsevol altre cosa
                return StatusCode(500, new { error = "Error intern del servidor." });
            }
        }

        // GET: api/collections
        [HttpGet]
        public async Task<ActionResult<IEnumerable<Collection>>> GetAll()
        {
            var collections = await _collectionService.GetAllCollectionsAsync();
            return Ok(collections);
        }

        // GET: api/collections/{id}
        [HttpGet("{id}")]
        public async Task<ActionResult<Collection>> GetById(Guid id)
        {
            var collection = await _collectionService.GetCollectionByIdAsync(id);
            if (collection == null) return NotFound();
            return Ok(collection);
        }

        // DELETE: api/collections/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(Guid id)
        {
            var deleted = await _collectionService.DeleteCollectionAsync(id);
            if (!deleted) return NotFound();
            return NoContent();
        }

        [HttpPut("{id}")]
        public async Task<ActionResult<Collection>> Update(Guid id, [FromBody] UpdateCollectionDto dto)
        {
            try
            {
                var updated = await _collectionService.UpdateCollectionAsync(id, dto);
                if (updated == null)
                    return NotFound();

                return Ok(updated);
            }
            catch (InvalidOperationException ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }
    }
}
