using Frikollection_Api.DTOs.Product;
using Frikollection_Api.Infraestructure;
using Frikollection_Api.Models;
using Microsoft.EntityFrameworkCore;

namespace Frikollection_Api.Services
{
    public class ProductService : IProductService
    {
        private readonly FrikollectionContext _context;

        public ProductService(FrikollectionContext context)
        {
            _context = context;
        }

        public async Task<Product> CreateProductAsync(CreateProductDto dto)
        {
            var product = new Product
            {
                ProductId = Guid.NewGuid(),
                Name = dto.Name,
                License = dto.License,
                Exclusive = dto.Exclusive,
                Status = dto.Status,
                Type = dto.Type,
                Subtype = dto.Subtype,
                ItemNumber = dto.ItemNumber,
                Value = dto.Value,
                Width = dto.Width,
                Height = dto.Height,
                Release = dto.Release,
                SmallPicture = dto.SmallPictureUrl,
                BigPicture = dto.BigPictureUrl,
                ProductTypeId = dto.ProductTypeId,
                ProductExtensionId = dto.ProductExtensionId
            };

            _context.Products.Add(product);

            // Assignar etiquetes (relació many-to-many)
            if (dto.TagIds != null && dto.TagIds.Any())
            {
                var tags = await _context.Tags
                    .Where(t => dto.TagIds.Contains(t.TagId))
                    .ToListAsync();

                foreach (var tag in tags)
                {
                    product.Tags.Add(tag);
                }
            }

            await _context.SaveChangesAsync();
            return product;
        }

        public async Task<Product?> GetByIdAsync(Guid id)
        {
            return await _context.Products
                .Include(p => p.Tags)
                .Include(p => p.ProductType)
                .Include(p => p.ProductExtension)
                .FirstOrDefaultAsync(p => p.ProductId == id);
        }

        public async Task<IEnumerable<Product>> GetAllAsync()
        {
            return await _context.Products
                .Include(p => p.Tags)
                .Include(p => p.ProductType)
                .Include(p => p.ProductExtension)
                .ToListAsync();
        }

        public async Task<bool> UpdateProductAsync(UpdateProductDto dto)
        {
            var product = await _context.Products
                .Include(p => p.Tags)
                .FirstOrDefaultAsync(p => p.ProductId == dto.ProductId);

            if (product == null)
                return false;

            product.Name = dto.Name;
            product.License = dto.License;
            product.Exclusive = dto.Exclusive;
            product.Status = dto.Status;
            product.Type = dto.Type;
            product.Subtype = dto.Subtype;
            product.ItemNumber = dto.ItemNumber;
            product.Value = dto.Value;
            product.Width = dto.Width;
            product.Height = dto.Height;
            product.Release = dto.Release;
            product.SmallPicture = dto.SmallPictureUrl;
            product.BigPicture = dto.BigPictureUrl;
            product.ProductTypeId = dto.ProductTypeId;
            product.ProductExtensionId = dto.ProductExtensionId;

            if (dto.TagIds != null)
            {
                var tags = await _context.Tags
                    .Where(t => dto.TagIds.Contains(t.TagId))
                    .ToListAsync();

                product.Tags.Clear();
                foreach (var tag in tags)
                {
                    product.Tags.Add(tag);
                }
            }

            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<ProductType?> GetProductTypeByIdAsync(Guid id)
        {
            return await _context.ProductTypes.FindAsync(id);
        }

        public async Task<ProductExtension?> GetProductExtensionByIdAsync(Guid id)
        {
            return await _context.ProductExtensions.FindAsync(id);
        }

        public async Task<bool> DeleteProductAsync(Guid id)
        {
            var product = await _context.Products
                .FirstOrDefaultAsync(p => p.ProductId == id);

            if (product == null)
                return false;

            _context.Products.Remove(product);
            await _context.SaveChangesAsync();
            return true;
        }
    }
}
