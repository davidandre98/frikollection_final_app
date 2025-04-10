using Frikollection_Api.DTOs.ProductExtension;
using Frikollection_Api.Models;

namespace Frikollection_Api.Services
{
    public interface IProductExtensionService
    {
        Task<ProductExtension?> CreateAsync(CreateProductExtensionDto dto);
        Task<IEnumerable<ProductExtension>> GetAllAsync();
        Task<ProductExtension?> GetByIdAsync(Guid id);
        Task<ProductExtension?> UpdateAsync(Guid id, UpdateProductExtensionDto dto);
        Task<bool> DeleteAsync(Guid id);
        ProductExtensionDto ToDto(ProductExtension extension);
    }
}
