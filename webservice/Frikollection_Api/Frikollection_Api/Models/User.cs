using System;
using System.Collections.Generic;

namespace Frikollection_Api.Models;

public partial class User
{
    public Guid UserId { get; set; }

    public string Username { get; set; } = null!;

    public string? Avatar { get; set; }

    public string? Nickname { get; set; }

    public string? FirstName { get; set; }

    public string? LastName { get; set; }

    public string Email { get; set; } = null!;

    public string? PhoneNumber { get; set; }

    public DateOnly? Birthday { get; set; }

    public string Password { get; set; } = null!;

    public string? Biography { get; set; }

    public DateTime? RegisterDate { get; set; }

    public DateTime? LastLogin { get; set; }

    public virtual ICollection<Collection> Collections { get; set; } = new List<Collection>();

    public virtual ICollection<Notification> NotificationFollowerUsers { get; set; } = new List<Notification>();

    public virtual ICollection<Notification> NotificationRecipientUsers { get; set; } = new List<Notification>();

    public virtual ICollection<UserFollowCollection> UserFollowCollections { get; set; } = new List<UserFollowCollection>();
}
