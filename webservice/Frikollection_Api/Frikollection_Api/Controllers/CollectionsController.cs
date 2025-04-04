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
            if (collection == null)
                return NotFound(new { message = "Col·lecció no trobada." });

            return Ok(collection);
        }

        // DELETE: api/collections/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(Guid id)
        {
            var result = await _collectionService.DeleteCollectionAsync(id);
            if (!result)
                return NotFound(new { message = "Col·lecció no trobada." });

            return NoContent();
        }

        // PUT: api/collections/{id}
        [HttpPut("{id}")]
        public async Task<ActionResult<Collection>> Update(Guid id, [FromBody] UpdateCollectionDto dto)
        {
            try
            {
                var updated = await _collectionService.UpdateCollectionAsync(id, dto);
                if (updated == null)
                    return NotFound(new { message = "Col·lecció no trobada." });

                return Ok(updated);
            }
            catch (InvalidOperationException ex)
            {
                return BadRequest(new { error = ex.Message });
            }
        }

        // POST: api/collections/{id}/follow
        [HttpPost("{id}/follow")]
        public async Task<IActionResult> FollowCollection(Guid id, [FromQuery] Guid userId)
        {
            var result = await _collectionService.FollowCollectionAsync(userId, id);

            if (!result)
                return BadRequest(new { message = "No s'ha pogut seguir la col·lecció. Potser no existeix o és privada." });

            return Ok(new { message = "Col·lecció seguida correctament." });
        }

        // DELETE: api/collections/{id}/follow
        [HttpDelete("{id}/follow")]
        public async Task<IActionResult> UnfollowCollection(Guid id, [FromQuery] Guid userId)
        {
            var result = await _collectionService.UnfollowCollectionAsync(userId, id);

            if (!result)
                return NotFound(new { message = "No estàs seguint aquesta col·lecció o no existeix." });

            return Ok(new { message = "Has deixat de seguir la col·lecció." });
        }

        // POST: api/collections/{id}/products
        [HttpPost("{id}/products")]
        public async Task<IActionResult> AddProductToCollection(Guid id, [FromBody] AddProductToCollectionDto dto)
        {
            if (id != dto.CollectionId)
                return BadRequest(new { message = "El collectionId del cos no coincideix amb el de la ruta." });

            var result = await _collectionService.AddProductToCollectionAsync(dto);
            if (!result)
                return Conflict(new { message = "Aquest producte ja forma part de la col·lecció." });

            return Ok(new { message = "Producte afegit correctament a la col·lecció." });
        }

        // DELETE: api/collections/{id}/products/{productId}
        [HttpDelete("{id}/products/{productId}")]
        public async Task<IActionResult> RemoveProductFromCollection(Guid id, Guid productId)
        {
            var result = await _collectionService.RemoveProductFromCollectionAsync(id, productId);
            if (!result)
                return NotFound(new { message = "El producte no existeix dins d’aquesta col·lecció." });

            return Ok(new { message = "Producte eliminat de la col·lecció." });
        }

        // GET: api/collections/{id}/products
        [HttpGet("{id}/products")]
        public async Task<IActionResult> GetProductsInCollection(Guid id)
        {
            var products = await _collectionService.GetProductsInCollectionAsync(id);
            return Ok(products);
        }

        // GET: api/collections/{id}/stats
        [HttpGet("{id}/stats")]
        public async Task<IActionResult> GetCollectionStats(Guid id)
        {
            var stats = await _collectionService.GetCollectionStatsAsync(id);
            return Ok(stats);
        }
    }
}
