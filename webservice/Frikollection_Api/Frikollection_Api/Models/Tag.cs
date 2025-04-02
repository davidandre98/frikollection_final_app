using System;
using System.Collections.Generic;

namespace Frikollection_Api.Models;

public partial class Tag
{
    public Guid TagId { get; set; }

    public string Name { get; set; } = null!;

    public string? TagImage { get; set; }

    public virtual ICollection<Product> Products { get; set; } = new List<Product>();
}
