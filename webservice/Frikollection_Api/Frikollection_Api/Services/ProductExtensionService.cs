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

        public async Task<ProductExtension?> CreateAsync(CreateProductExtensionDto dto)
        {
            var product = await _context.Products.FirstOrDefaultAsync(p => p.Name == dto.Name);
            if (product == null) return null;

            var extension = new ProductExtension
            {
                ProductExtensionId = Guid.NewGuid(),
                Hp = dto.Hp,
                PokemonTypes = dto.PokemonTypes != null ? string.Join(",", dto.PokemonTypes) : null,
                EvolvesFrom = dto.EvolvesFrom,
                Abilities = dto.Abilities != null ? System.Text.Json.JsonSerializer.Serialize(dto.Abilities) : null,
                Attacks = dto.Attacks != null ? System.Text.Json.JsonSerializer.Serialize(dto.Attacks) : null,
                ConvertedRetreatCost = dto.ConvertedRetreatCost,
                Package = dto.Set?.Name,
                Expansion = dto.Set?.Series
            };

            // Assignar ProductExtensionId a Product
            product.ProductExtensionId = extension.ProductExtensionId;

            // Assignar Release a Product
            if (DateOnly.TryParse(dto.Set?.ReleaseDate, out var releaseDate))
                product.Release = releaseDate;

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
            extension.PokemonTypes = dto.PokemonTypes != null ? string.Join(",", dto.PokemonTypes) : null;
            extension.EvolvesFrom = dto.EvolvesFrom;
            extension.Abilities = dto.Abilities != null ? System.Text.Json.JsonSerializer.Serialize(dto.Abilities) : null;
            extension.Attacks = dto.Attacks != null ? System.Text.Json.JsonSerializer.Serialize(dto.Attacks) : null;
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

        public CreateProductExtensionDto MapFromJson(CreateProductExtensionDto dto)
        {
            return new CreateProductExtensionDto
            {
                Name = dto.Name,
                Hp = dto.Hp,
                PokemonTypes = dto.PokemonTypes,
                EvolvesFrom = dto.EvolvesFrom,
                Abilities = dto.Abilities,
                Attacks = dto.Attacks,
                ConvertedRetreatCost = dto.ConvertedRetreatCost,
                Set = dto.Set != null ? new CreateProductExtensionDto.SetDto
                {
                    Name = dto.Set.Name,
                    Series = dto.Set.Series,
                    ReleaseDate = dto.Set.ReleaseDate
                } : null,
                Cardmarket = dto.Cardmarket != null ? new CreateProductExtensionDto.CardmarketDto
                {
                    Prices = dto.Cardmarket.Prices != null ? new CreateProductExtensionDto.CardmarketDto.CardmarketPricesDto
                    {
                        AverageSellPrice = dto.Cardmarket.Prices.AverageSellPrice
                    } : null
                } : null
            };
        }
    }
}
