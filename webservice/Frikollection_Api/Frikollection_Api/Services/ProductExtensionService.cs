using Frikollection_Api.DTOs.ProductExtension;
using Frikollection_Api.Infraestructure;
using Frikollection_Api.Models;
using Microsoft.EntityFrameworkCore;
using Microsoft.IdentityModel.Tokens;
using System.Text.Json;

namespace Frikollection_Api.Services
{
    public class ProductExtensionService : IProductExtensionService
    {
        private readonly FrikollectionContext _context;

        public ProductExtensionService(FrikollectionContext context)
        {
            _context = context;
        }

        public async Task<ProductExtension?> CreateAsync(CreateProductExtensionDto dto)
        {
            var product = await _context.Products.FirstOrDefaultAsync(p => p.Name == dto.Name);
            if (product == null) return null;

            var extension = new ProductExtension
            {
                ProductExtensionId = Guid.NewGuid(),
                Hp = dto.Hp,
                PokemonTypes = dto.Types != null ? string.Join(",", dto.Types) : null,
                EvolvesFrom = dto.EvolvesFrom,
                Abilities = dto.Abilities != null ? JsonSerializer.Serialize(dto.Abilities) : null,
                Attacks = dto.Attacks != null ? JsonSerializer.Serialize(dto.Attacks) : null,
                ConvertedRetreatCost = dto.ConvertedRetreatCost,
                Package = dto.Set?.Name,
                Expansion = dto.Set?.Series
            };

            // Assignar ProductExtensionId a Product
            product.ProductExtensionId = extension.ProductExtensionId;

            // Assignar Supertype a Product
            if (!dto.Supertype.IsNullOrEmpty())
                product.Supertype = dto.Supertype;

            // Assignar Subtype a Product
            if (!dto.Rarity.IsNullOrEmpty())
                product.Subtype = dto.Rarity;

            // Assignar Release a Product
            if (DateOnly.TryParse(dto.Set?.ReleaseDate, out var releaseDate))
                product.Release = releaseDate;

            // Assignar ItemNumber a Product
            if (dto.NationalPokedexNumbers != null && dto.NationalPokedexNumbers.Any())
                product.ItemNumber = dto.NationalPokedexNumbers.First();

            // Assignar Value a Product
            product.Value = dto.Cardmarket?.Prices?.AverageSellPrice;

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

        public async Task<ProductExtension?> UpdateAsync(Guid id, UpdateProductExtensionDto dto)
        {
            var extension = await _context.ProductExtensions.FindAsync(id);
            if (extension == null) return null;

            extension.Hp = dto.Hp;
            extension.PokemonTypes = dto.Types != null ? string.Join(",", dto.Types) : null;
            extension.EvolvesFrom = dto.EvolvesFrom;
            extension.Abilities = dto.Abilities != null ? JsonSerializer.Serialize(dto.Abilities) : null;
            extension.Attacks = dto.Attacks != null ? JsonSerializer.Serialize(dto.Attacks) : null;
            extension.ConvertedRetreatCost = dto.ConvertedRetreatCost;
            extension.Package = dto.Package;
            extension.Expansion = dto.Expansion;

            await _context.SaveChangesAsync();
            return extension;
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

        public ProductExtensionDto ToDto(ProductExtension ext)
        {
            return new ProductExtensionDto
            {
                Hp = ext.Hp,
                PokemonTypes = ext.PokemonTypes,
                EvolvesFrom = ext.EvolvesFrom,
                Abilities = string.IsNullOrEmpty(ext.Abilities)
                    ? null
                    : JsonSerializer.Deserialize<List<AbilityDto>>(ext.Abilities),
                Attacks = string.IsNullOrEmpty(ext.Attacks)
                    ? null
                    : JsonSerializer.Deserialize<List<AttackDto>>(ext.Attacks),
                ConvertedRetreatCost = ext.ConvertedRetreatCost,
                Package = ext.Package,
                Expansion = ext.Expansion
            };
        }
    }
}
