using Frikollection_Api.DTOs.ProductExtension;
using Frikollection_Api.Helpers;
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
        private readonly IHttpContextAccessor _httpContextAccessor;

        public ProductExtensionService(FrikollectionContext context, IHttpContextAccessor httpContextAccessor)
        {
            _context = context;
            _httpContextAccessor = httpContextAccessor;
        }

        public async Task<ProductExtension?> CreateAsync(CreateProductExtensionDto dto)
        {
            var product = await _context.Products
                .Include(p => p.ProductType)
                .FirstOrDefaultAsync(p => p.Name == dto.Name);
            if (product == null) return null;

            if (product.ProductType == null || product.ProductType.HasExtension != true)
                return null;

            var extension = new ProductExtension
            {
                ProductExtensionId = Guid.NewGuid(),
                Hp = dto.Hp,
                PokemonTypes = dto.Types != null ? string.Join(",", dto.Types) : null,
                EvolvesFrom = dto.EvolvesFrom,
                Abilities = dto.Abilities != null ? JsonSerializer.Serialize(dto.Abilities) : null,
                Attacks = dto.Attacks != null ? JsonSerializer.Serialize(dto.Attacks) : null,
                ConvertedRetreatCost = dto.ConvertedRetreatCost ?? 0,
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

            // Desar imatges al servidor i assignar el path absolut
            if (dto.Images != null)
            {
                var request = _httpContextAccessor.HttpContext?.Request;
                var host = request != null ? $"{request.Scheme}://{request.Host}" : "";

                var baseImageName = $"{dto.Name}_{dto.Set?.Name}_{dto.Set?.Series}";

                var smallPath = await ImageHelper.DownloadAndSaveImageAsync(dto.Images.Small, baseImageName);
                var largePath = await ImageHelper.DownloadAndSaveImageAsync(dto.Images.Large, baseImageName + "_large");

                if (smallPath != null)
                    product.SmallPicture = host + smallPath;

                if (largePath != null)
                    product.BigPicture = host + largePath;
            }

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

            if (dto.Hp.HasValue)
                extension.Hp = dto.Hp;

            if (dto.Types != null)
                extension.PokemonTypes = string.Join(",", dto.Types);

            if (dto.EvolvesFrom != null)
                extension.EvolvesFrom = dto.EvolvesFrom;

            if (dto.Abilities != null)
                extension.Abilities = JsonSerializer.Serialize(dto.Abilities);

            if (dto.Attacks != null)
                extension.Attacks = JsonSerializer.Serialize(dto.Attacks);

            if (dto.ConvertedRetreatCost.HasValue)
                extension.ConvertedRetreatCost = dto.ConvertedRetreatCost;

            if (dto.Package != null)
                extension.Package = dto.Package;

            if (dto.Expansion != null)
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
