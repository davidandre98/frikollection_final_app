using Frikollection_Api.DTOs.ProductExtension;
using Frikollection_Api.Models;

namespace Frikollection_Api.Services
{
    public interface IProductExtensionService
    {
        Task<ProductExtension> CreateAsync(CreateProductExtensionDto dto);
        Task<IEnumerable<ProductExtension>> GetAllAsync();
        Task<ProductExtension?> GetByIdAsync(Guid id);
        Task<bool> UpdateAsync(UpdateProductExtensionDto dto);
        Task<bool> DeleteAsync(Guid id);
    }
}
