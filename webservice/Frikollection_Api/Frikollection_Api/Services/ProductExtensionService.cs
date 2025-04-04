using Frikollection_Api.DTOs.ProductExtension;
using Frikollection_Api.Infraestructure;
using Frikollection_Api.Models;
using Microsoft.EntityFrameworkCore;

namespace Frikollection_Api.Services
{
    public class ProductExtensionService : IProductExtensionService
    {
        private readonly FrikollectionContext _context;

        public ProductExtensionService(FrikollectionContext context)
        {
            _context = context;
        }

        public async Task<ProductExtension> CreateAsync(CreateProductExtensionDto dto)
        {
            var extension = new ProductExtension
            {
                ProductExtensionId = Guid.NewGuid(),
                Expansion = dto.Expansion,
                Package = dto.Package
            };

            _context.ProductExtensions.Add(extension);
            await _context.SaveChangesAsync();
            return extension;
        }

        public async Task<IEnumerable<ProductExtension>> GetAllAsync()
        {
            return await _context.ProductExtensions.ToListAsync();
        }

        public async Task<ProductExtension?> GetByIdAsync(Guid id)
        {
            return await _context.ProductExtensions.FindAsync(id);
        }

        public async Task<bool> UpdateAsync(UpdateProductExtensionDto dto)
        {
            var extension = await _context.ProductExtensions.FindAsync(dto.ProductExtensionId);
            if (extension == null) return false;

            extension.Expansion = dto.Expansion;
            extension.Package = dto.Package;

            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> DeleteAsync(Guid id)
        {
            var extension = await _context.ProductExtensions
                .Include(e => e.Products)
                .FirstOrDefaultAsync(e => e.ProductExtensionId == id);

            if (extension == null || extension.Products.Any())
                return false; // Evitem eliminar si està en ús

            _context.ProductExtensions.Remove(extension);
            await _context.SaveChangesAsync();
            return true;
        }
    }
}
