using System;
using System.Collections.Generic;

namespace Frikollection_Api.Models;

public partial class ProductExtension
{
    public Guid ProductExtensionId { get; set; }

    public string? Package { get; set; }

    public string? Expansion { get; set; }

    public int? Hp { get; set; }

    public string? PokemonTypes { get; set; }

    public string? EvolvesFrom { get; set; }

    public string? Abilities { get; set; }

    public string? Attacks { get; set; }

    public int? ConvertedRetreatCost { get; set; }

    public virtual ICollection<Product> Products { get; set; } = new List<Product>();
}
