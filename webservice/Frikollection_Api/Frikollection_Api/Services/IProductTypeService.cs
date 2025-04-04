using Frikollection_Api.DTOs.ProductType;
using Frikollection_Api.Models;

namespace Frikollection_Api.Services
{
    public interface IProductTypeService
    {
        Task<ProductType> CreateAsync(CreateProductTypeDto dto);
        Task<IEnumerable<ProductType>> GetAllAsync();
        Task<ProductType?> GetByIdAsync(Guid id);
        Task<bool> UpdateAsync(UpdateProductTypeDto dto);
        Task<bool> DeleteAsync(Guid id);
    }
}
