namespace Frikollection_Api.DTOs.Collection
{
    public class CollectionStatsDto
    {
        public Guid CollectionId { get; set; }
        public int TotalProducts { get; set; }
        public decimal TotalValue { get; set; }
        public Dictionary<string, int> ProductTypes { get; set; } = new();
    }
}
