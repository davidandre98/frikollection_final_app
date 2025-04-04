using System;
using System.Collections.Generic;

namespace Frikollection_Api.Models;

public partial class Notification
{
    public Guid NotificationId { get; set; }

    public Guid RecipientUserId { get; set; }

    public Guid FollowerUserId { get; set; }

    public Guid CollectionId { get; set; }

    public string Message { get; set; } = null!;

    public DateTime CreatedAt { get; set; }

    public bool IsRead { get; set; }

    public virtual Collection Collection { get; set; } = null!;

    public virtual User FollowerUser { get; set; } = null!;

    public virtual User RecipientUser { get; set; } = null!;
}
