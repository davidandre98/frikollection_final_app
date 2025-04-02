using System;
using System.Collections.Generic;

namespace Frikollection_Api.Models;

public partial class ProductType
{
    public Guid ProductTypeId { get; set; }

    public string TypeName { get; set; } = null!;

    public bool? HasExtension { get; set; }

    public virtual ICollection<Product> Products { get; set; } = new List<Product>();
}
