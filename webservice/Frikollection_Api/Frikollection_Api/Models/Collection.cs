using System;
using System.Collections.Generic;

namespace Frikollection_Api.Models;

public partial class Collection
{
    public Guid CollectionId { get; set; }

    public Guid UserId { get; set; }

    public string Name { get; set; } = null!;

    public bool? Private { get; set; }

    public DateOnly? CreationDate { get; set; }

    public virtual ICollection<CollectionProduct> CollectionProducts { get; set; } = new List<CollectionProduct>();

    public virtual ICollection<Notification> Notifications { get; set; } = new List<Notification>();

    public virtual User User { get; set; } = null!;

    public virtual ICollection<UserFollowCollection> UserFollowCollections { get; set; } = new List<UserFollowCollection>();
}
