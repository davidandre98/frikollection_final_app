using System;
using System.Collections.Generic;

namespace Frikollection_Api.Models;

public partial class Product
{
    public Guid ProductId { get; set; }

    public string Name { get; set; } = null!;

    public string? Type { get; set; }

    public string? Subtype { get; set; }

    public DateOnly? Release { get; set; }

    public string? Status { get; set; }

    public string? ItemNumber { get; set; }

    public string? License { get; set; }

    public double? Width { get; set; }

    public double? Height { get; set; }

    public decimal? Value { get; set; }

    public string? SmallPicture { get; set; }

    public string? BigPicture { get; set; }

    public Guid? ProductTypeId { get; set; }

    public Guid? ProductExtensionId { get; set; }

    public virtual ICollection<CollectionProduct> CollectionProducts { get; set; } = new List<CollectionProduct>();

    public virtual ProductExtension? ProductExtension { get; set; }

    public virtual ProductType? ProductType { get; set; }

    public virtual ICollection<Tag> Tags { get; set; } = new List<Tag>();
}
