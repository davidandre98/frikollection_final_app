using System;
using System.Collections.Generic;

namespace Frikollection_Api.Models;

public partial class ProductExtension
{
    public Guid ProductExtensionId { get; set; }

    public string? Package { get; set; }

    public string? Expansion { get; set; }

    public virtual ICollection<Product> Products { get; set; } = new List<Product>();
}
