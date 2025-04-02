using System;
using System.Collections.Generic;

namespace Frikollection_Api.Models;

public partial class CollectionProduct
{
    public Guid CollectionId { get; set; }

    public Guid ProductId { get; set; }

    public DateOnly? AddedDate { get; set; }

    public virtual Collection Collection { get; set; } = null!;

    public virtual Product Product { get; set; } = null!;
}
