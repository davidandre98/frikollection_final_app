using Frikollection_Api.DTOs.ProductType;
using Frikollection_Api.Infraestructure;
using Frikollection_Api.Models;
using Microsoft.EntityFrameworkCore;

namespace Frikollection_Api.Services
{
    public class ProductTypeService : IProductTypeService
    {
        private readonly FrikollectionContext _context;

        public ProductTypeService(FrikollectionContext context)
        {
            _context = context;
        }

        public async Task<IEnumerable<ProductType>> GetAllAsync()
        {
            return await _context.ProductTypes.ToListAsync();
        }

        public async Task<ProductType?> GetByIdAsync(Guid id)
        {
            return await _context.ProductTypes.FindAsync(id);
        }

        public async Task<ProductType> CreateAsync(CreateProductTypeDto dto)
        {
            var type = new ProductType
            {
                ProductTypeId = Guid.NewGuid(),
                TypeName = dto.TypeName,
                HasExtension = dto.HasExtension
            };

            _context.ProductTypes.Add(type);
            await _context.SaveChangesAsync();
            return type;
        }

        public async Task<bool> UpdateAsync(UpdateProductTypeDto dto)
        {
            var type = await _context.ProductTypes.FindAsync(dto.ProductTypeId);
            if (type == null) return false;

            type.TypeName = dto.TypeName;
            type.HasExtension = dto.HasExtension;

            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> DeleteAsync(Guid id)
        {
            var type = await _context.ProductTypes
                .Include(t => t.Products)
                .FirstOrDefaultAsync(t => t.ProductTypeId == id);

            if (type == null || type.Products.Any())
                return false; // Evitem eliminar si té productes associats

            _context.ProductTypes.Remove(type);
            await _context.SaveChangesAsync();
            return true;
        }
    }
}
