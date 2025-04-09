using System.ComponentModel.DataAnnotations;

namespace Frikollection_Api.DTOs.ProductExtension
{
    public class CreateProductExtensionDto
    {
        [Required]
        public string Name { get; set; } // Per vincular-lo al 'name' de Product

        public int? Hp { get; set; }

        public List<string>? PokemonTypes { get; set; }

        public string? EvolvesFrom { get; set; }

        public List<AbilityDto>? Abilities { get; set; }

        public List<AttackDto>? Attacks { get; set; }

        public int? ConvertedRetreatCost { get; set; }

        public SetDto? Set { get; set; }

        public CardmarketDto? Cardmarket { get; set; }

        public class SetDto
        {
            public string Name { get; set; }
            public string Series { get; set; }
            public string ReleaseDate { get; set; }
        }

        public class CardmarketDto
        {
            public CardmarketPricesDto Prices { get; set; }

            public class CardmarketPricesDto
            {
                public decimal? AverageSellPrice { get; set; }
            }
        }
    }
}