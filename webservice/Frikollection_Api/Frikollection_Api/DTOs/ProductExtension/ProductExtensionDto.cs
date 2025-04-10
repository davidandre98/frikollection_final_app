namespace Frikollection_Api.DTOs.ProductExtension
{
    public class ProductExtensionDto
    {
        public int? Hp { get; set; }
        public string? PokemonTypes { get; set; }
        public string? EvolvesFrom { get; set; }
        public List<AbilityDto>? Abilities { get; set; }
        public List<AttackDto>? Attacks { get; set; }
        public int? ConvertedRetreatCost { get; set; }
        public string? Package { get; set; }
        public string? Expansion { get; set; }
    }
}
