using Frikollection_Api.DTOs.Product;
using Frikollection_Api.DTOs.ProductExtension;
using Frikollection_Api.Models;

namespace Frikollection_Api.Services
{
    public interface IProductService
    {
        Task<Product> CreateProductAsync(CreateProductDto dto);
        Task<Product?> GetByIdAsync(Guid id);
        Task<IEnumerable<Product>> GetAllAsync();
        Task<bool> UpdateProductAsync(UpdateProductDto dto);
        Task<ProductType?> GetProductTypeByIdAsync(Guid id);
        Task<ProductExtension?> GetProductExtensionByIdAsync(Guid id);
        Task<bool> DeleteProductAsync(Guid id);
        ProductDto ToDto(Product product);
    }
}
