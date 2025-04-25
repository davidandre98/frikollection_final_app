using Frikollection_Api.DTOs.Product;
using Frikollection_Api.DTOs.ProductExtension;
using Frikollection_Api.DTOs.ProductType;
using Frikollection_Api.DTOs.Tag;
using Frikollection_Api.Infraestructure;
using Frikollection_Api.Models;
using Microsoft.EntityFrameworkCore;
using System.Text.Json;

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
            if (dto.ProductExtensionId != null && dto.ProductTypeId != null)
            {
                var type = await _context.ProductTypes.FindAsync(dto.ProductTypeId);
                if (type != null && type.HasExtension == false)
                {
                    throw new InvalidOperationException("Aquest tipus de producte no admet extensió.");
                }
            }

            var product = new Product
            {
                ProductId = Guid.NewGuid(),
                Name = dto.Name,
                License = dto.License,
                Status = dto.Status,
                Supertype = dto.Supertype,
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

            return await _context.Products
                .Include(p => p.ProductType)
                .Include(p => p.ProductExtension)
                .Include(p => p.Tags)
                .FirstOrDefaultAsync(p => p.ProductId == product.ProductId);
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

            bool hasExtension = product.ProductExtensionId != null || dto.ProductExtensionId != null;
            bool hasProductType = product.ProductType != null || dto.ProductTypeId != null;
            if (hasExtension && hasProductType)
            {
                var type = product.ProductType;
                if (dto.ProductTypeId != null && (type == null || dto.ProductTypeId != type.ProductTypeId))
                {
                    type = await _context.ProductTypes.FindAsync(dto.ProductTypeId);
                }

                if (type != null && !type.HasExtension.GetValueOrDefault())
                {
                    throw new InvalidOperationException("Aquest tipus de producte no admet extensió.");
                }
            }

            product.Name = dto.Name;
            product.License = dto.License;
            product.Status = dto.Status;
            product.Supertype = dto.Supertype;
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

        public ProductDto ToDto(Product product)
        {
            return new ProductDto
            {
                Name = product.Name,
                Supertype = product.Supertype,
                Subtype = product.Subtype,
                Release = product.Release,
                Status = product.Status,
                ItemNumber = product.ItemNumber,
                License = product.License,
                Width = product.Width,
                Height = product.Height,
                Value = product.Value,
                SmallPicture = product.SmallPicture,
                BigPicture = product.BigPicture,
                ProductType = product.ProductType != null
                    ? new ProductTypeDto
                    {
                        TypeName = product.ProductType.TypeName,
                        HasExtension = product.ProductType.HasExtension
                    }
                    : null,
                ProductExtension = product.ProductExtension != null
                    ? new ProductExtensionDto
                    {
                        Hp = product.ProductExtension.Hp,
                        PokemonTypes = product.ProductExtension.PokemonTypes,
                        EvolvesFrom = product.ProductExtension.EvolvesFrom,
                        Abilities = string.IsNullOrEmpty(product.ProductExtension.Abilities)
                            ? null
                            : JsonSerializer.Deserialize<List<AbilityDto>>(product.ProductExtension.Abilities),
                        Attacks = string.IsNullOrEmpty(product.ProductExtension.Attacks)
                            ? null
                            : JsonSerializer.Deserialize<List<AttackDto>>(product.ProductExtension.Attacks),
                        ConvertedRetreatCost = product.ProductExtension.ConvertedRetreatCost,
                        Package = product.ProductExtension.Package,
                        Expansion = product.ProductExtension.Expansion
                    }
                    : null,
                Tags = product.Tags.Select(t => new TagDto
                {
                    Name = t.Name,
                    TagImage = t.TagImage
                }).ToList()
            };
        }
    }
}
