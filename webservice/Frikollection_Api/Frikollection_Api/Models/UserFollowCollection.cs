using System;
using System.Collections.Generic;

namespace Frikollection_Api.Models;

public partial class UserFollowCollection
{
    public Guid UserId { get; set; }

    public Guid CollectionId { get; set; }

    public DateTime? FollowDate { get; set; }

    public bool? NotificationsEnabled { get; set; }

    public virtual Collection Collection { get; set; } = null!;

    public virtual User User { get; set; } = null!;
}
