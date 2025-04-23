using Frikollection_Api.DTOs.Product;
using Frikollection_Api.Services;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace Frikollection_Api.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class ProductsController : ControllerBase
    {
        private readonly IProductService _productService;

        public ProductsController(IProductService productService)
        {
            _productService = productService;
        }

        // POST: api/products
        [HttpPost]
        public async Task<IActionResult> CreateProduct([FromBody] CreateProductDto dto)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            if (dto.ProductExtensionId != null && dto.ProductTypeId != null)
            {
                var type = await _productService.GetProductTypeByIdAsync(dto.ProductTypeId.Value);
                if (type != null && type.HasExtension == false)
                {
                    return BadRequest(new { message = "Aquest tipus de producte no admet extensió." });
                }

                var extension = await _productService.GetProductExtensionByIdAsync(dto.ProductExtensionId.Value);
                if (extension == null)
                {
                    return BadRequest(new { message = "Extensió no vàlida." });
                }
            }

            var product = await _productService.CreateProductAsync(dto);
            var dtoResult = _productService.ToDto(product);
            return Ok(dtoResult);
        }

        // POST: api/products/create-multiple
        [HttpPost("create-multiple")]
        public async Task<IActionResult> CreateMultipleProducts([FromBody] List<CreateProductDto> productDtos)
        {
            if (!ModelState.IsValid)
                return BadRequest(ModelState);

            var createdProducts = new List<ProductDto>();
            var errors = new List<object>();

            foreach (var dto in productDtos)
            {
                if (dto.ProductExtensionId != null && dto.ProductTypeId != null)
                {
                    var type = await _productService.GetProductTypeByIdAsync(dto.ProductTypeId.Value);
                    if (type != null && type.HasExtension == false)
                    {
                        errors.Add(new { dto.Name, message = "Aquest tipus de producte no admet extensió." });
                        continue;
                    }

                    var extension = await _productService.GetProductExtensionByIdAsync(dto.ProductExtensionId.Value);
                    if (extension == null)
                    {
                        errors.Add(new { dto.Name, message = "Extensió no vàlida." });
                        continue;
                    }
                }

                try
                {
                    var product = await _productService.CreateProductAsync(dto);
                    createdProducts.Add(_productService.ToDto(product));
                }
                catch (Exception ex)
                {
                    errors.Add(new { dto.Name, message = "Error creant el producte", details = ex.Message });
                }
            }

            return Ok(new { created = createdProducts, errors });
        }

        // GET: api/products/{id}
        [HttpGet("{id}")]
        public async Task<IActionResult> GetProductById(Guid id)
        {
            var product = await _productService.GetByIdAsync(id);
            if (product == null)
                return NotFound(new { message = "Producte no trobat." });

            var dto = _productService.ToDto(product);
            return Ok(dto);
        }

        // GET: api/products
        [HttpGet]
        public async Task<IActionResult> GetAllProducts()
        {
            var products = await _productService.GetAllAsync();
            var dtoResult = products.Select(_productService.ToDto);
            return Ok(dtoResult);
        }

        // PUT: api/products/{id}
        [HttpPut("{id}")]
        public async Task<IActionResult> UpdateProduct(Guid id, [FromBody] UpdateProductDto dto)
        {
            if (id != dto.ProductId)
                return BadRequest(new { message = "L'ID del producte no coincideix amb el cos de la petició." });

            if (dto.ProductExtensionId != null && dto.ProductTypeId != null)
            {
                var type = await _productService.GetProductTypeByIdAsync(dto.ProductTypeId.Value);
                if (type != null && type.HasExtension == false)
                {
                    return BadRequest(new { message = "Aquest tipus de producte no admet extensió." });
                }

                var extension = await _productService.GetProductExtensionByIdAsync(dto.ProductExtensionId.Value);
                if (extension == null)
                {
                    return BadRequest(new { message = "Extensió no vàlida." });
                }
            }

            var updated = await _productService.UpdateProductAsync(dto);
            if (!updated)
                return NotFound(new { message = "Producte no trobat." });

            return Ok(new { message = "Producte actualitzat correctament." });
        }

        // DELETE: api/products/{id}
        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteProduct(Guid id)
        {
            var deleted = await _productService.DeleteProductAsync(id);
            if (!deleted)
                return NotFound(new { message = "Producte no trobat." });

            return Ok(new { message = "Producte eliminat correctament." });
        }
    }
}
