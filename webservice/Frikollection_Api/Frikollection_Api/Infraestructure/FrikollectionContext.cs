using System;
using System.Collections.Generic;
using Frikollection_Api.Models;
using Microsoft.EntityFrameworkCore;

namespace Frikollection_Api.Infraestructure;

public partial class FrikollectionContext : DbContext
{
    public FrikollectionContext()
    {
    }

    public FrikollectionContext(DbContextOptions<FrikollectionContext> options)
        : base(options)
    {
    }

    public virtual DbSet<Collection> Collections { get; set; }

    public virtual DbSet<CollectionProduct> CollectionProducts { get; set; }

    public virtual DbSet<Product> Products { get; set; }

    public virtual DbSet<ProductExtension> ProductExtensions { get; set; }

    public virtual DbSet<ProductType> ProductTypes { get; set; }

    public virtual DbSet<Tag> Tags { get; set; }

    public virtual DbSet<User> Users { get; set; }

    public virtual DbSet<UserFollowCollection> UserFollowCollections { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.Entity<Collection>(entity =>
        {
            entity.HasKey(e => e.CollectionId).HasName("PK__Collecti__53D3A5CAE93E11F4");

            entity.ToTable("Collection", tb => tb.HasTrigger("TR_DeleteCollectionFollowings"));

            entity.Property(e => e.CollectionId)
                .HasDefaultValueSql("(newid())")
                .HasColumnName("collection_id");
            entity.Property(e => e.CreationDate)
                .HasDefaultValueSql("(getdate())")
                .HasColumnName("creation_date");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .HasColumnName("name");
            entity.Property(e => e.Private)
                .HasDefaultValue(false)
                .HasColumnName("private");
            entity.Property(e => e.UserId).HasColumnName("user_id");

            entity.HasOne(d => d.User).WithMany(p => p.Collections)
                .HasForeignKey(d => d.UserId)
                .HasConstraintName("FK__Collectio__user___403A8C7D");
        });

        modelBuilder.Entity<CollectionProduct>(entity =>
        {
            entity.HasKey(e => new { e.CollectionId, e.ProductId }).HasName("PK__Collecti__17A38215BBAC1509");

            entity.ToTable("Collection_Product");

            entity.Property(e => e.CollectionId).HasColumnName("collection_id");
            entity.Property(e => e.ProductId).HasColumnName("product_id");
            entity.Property(e => e.AddedDate)
                .HasDefaultValueSql("(getdate())")
                .HasColumnName("added_date");

            entity.HasOne(d => d.Collection).WithMany(p => p.CollectionProducts)
                .HasForeignKey(d => d.CollectionId)
                .HasConstraintName("FK__Collectio__colle__5535A963");

            entity.HasOne(d => d.Product).WithMany(p => p.CollectionProducts)
                .HasForeignKey(d => d.ProductId)
                .HasConstraintName("FK__Collectio__produ__5629CD9C");
        });

        modelBuilder.Entity<Product>(entity =>
        {
            entity.HasKey(e => e.ProductId).HasName("PK__Product__47027DF5B4D91685");

            entity.ToTable("Product");

            entity.Property(e => e.ProductId)
                .HasDefaultValueSql("(newid())")
                .HasColumnName("product_id");
            entity.Property(e => e.BigPicture).HasColumnName("big_picture");
            entity.Property(e => e.Height).HasColumnName("height");
            entity.Property(e => e.ItemNumber)
                .HasMaxLength(50)
                .HasColumnName("item_number");
            entity.Property(e => e.License)
                .HasMaxLength(100)
                .HasColumnName("license");
            entity.Property(e => e.Name)
                .HasMaxLength(100)
                .HasColumnName("name");
            entity.Property(e => e.ProductExtensionId).HasColumnName("product_extension_id");
            entity.Property(e => e.ProductTypeId).HasColumnName("product_type_id");
            entity.Property(e => e.Release).HasColumnName("release");
            entity.Property(e => e.SmallPicture).HasColumnName("small_picture");
            entity.Property(e => e.Status)
                .HasMaxLength(50)
                .HasColumnName("status");
            entity.Property(e => e.Subtype)
                .HasMaxLength(100)
                .HasColumnName("subtype");
            entity.Property(e => e.Type)
                .HasMaxLength(100)
                .HasColumnName("type");
            entity.Property(e => e.Value)
                .HasColumnType("decimal(10, 2)")
                .HasColumnName("value");
            entity.Property(e => e.Width).HasColumnName("width");

            entity.HasOne(d => d.ProductExtension).WithMany(p => p.Products)
                .HasForeignKey(d => d.ProductExtensionId)
                .HasConstraintName("FK__Product__product__5165187F");

            entity.HasOne(d => d.ProductType).WithMany(p => p.Products)
                .HasForeignKey(d => d.ProductTypeId)
                .HasConstraintName("FK__Product__product__5070F446");

            entity.HasMany(d => d.Tags).WithMany(p => p.Products)
                .UsingEntity<Dictionary<string, object>>(
                    "ProductTag",
                    r => r.HasOne<Tag>().WithMany()
                        .HasForeignKey("TagId")
                        .HasConstraintName("FK__Product_T__tag_i__5CD6CB2B"),
                    l => l.HasOne<Product>().WithMany()
                        .HasForeignKey("ProductId")
                        .HasConstraintName("FK__Product_T__produ__5BE2A6F2"),
                    j =>
                    {
                        j.HasKey("ProductId", "TagId").HasName("PK__Product___332B17DEAA44D592");
                        j.ToTable("Product_Tag");
                        j.IndexerProperty<Guid>("ProductId").HasColumnName("product_id");
                        j.IndexerProperty<Guid>("TagId").HasColumnName("tag_id");
                    });
        });

        modelBuilder.Entity<ProductExtension>(entity =>
        {
            entity.HasKey(e => e.ProductExtensionId).HasName("PK__Product___52E732FB88CA66FB");

            entity.ToTable("Product_Extension");

            entity.Property(e => e.ProductExtensionId)
                .HasDefaultValueSql("(newid())")
                .HasColumnName("product_extension_id");
            entity.Property(e => e.Expansion)
                .HasMaxLength(100)
                .HasColumnName("expansion");
            entity.Property(e => e.Package)
                .HasMaxLength(100)
                .HasColumnName("package");
        });

        modelBuilder.Entity<ProductType>(entity =>
        {
            entity.HasKey(e => e.ProductTypeId).HasName("PK__Product___6EED3ED6C6638524");

            entity.ToTable("Product_Type");

            entity.Property(e => e.ProductTypeId)
                .HasDefaultValueSql("(newid())")
                .HasColumnName("product_type_id");
            entity.Property(e => e.HasExtension)
                .HasDefaultValue(false)
                .HasColumnName("hasExtension");
            entity.Property(e => e.TypeName)
                .HasMaxLength(100)
                .HasColumnName("type_name");
        });

        modelBuilder.Entity<Tag>(entity =>
        {
            entity.HasKey(e => e.TagId).HasName("PK__Tag__4296A2B6B3272C9A");

            entity.ToTable("Tag");

            entity.Property(e => e.TagId)
                .HasDefaultValueSql("(newid())")
                .HasColumnName("tag_id");
            entity.Property(e => e.Name)
                .HasMaxLength(50)
                .HasColumnName("name");
            entity.Property(e => e.TagImage).HasColumnName("tag_image");
        });

        modelBuilder.Entity<User>(entity =>
        {
            entity.HasKey(e => e.UserId).HasName("PK__User__B9BE370F26E67DD3");

            entity.ToTable("User");

            entity.HasIndex(e => e.Email, "UQ__User__AB6E616497F1807D").IsUnique();

            entity.HasIndex(e => e.Username, "UQ__User__F3DBC5724D509274").IsUnique();

            entity.Property(e => e.UserId)
                .HasDefaultValueSql("(newid())")
                .HasColumnName("user_id");
            entity.Property(e => e.Avatar).HasColumnName("avatar");
            entity.Property(e => e.Biography).HasColumnName("biography");
            entity.Property(e => e.Birthday).HasColumnName("birthday");
            entity.Property(e => e.Email)
                .HasMaxLength(100)
                .HasColumnName("email");
            entity.Property(e => e.FirstName)
                .HasMaxLength(50)
                .HasColumnName("first_name");
            entity.Property(e => e.LastLogin)
                .HasColumnType("datetime")
                .HasColumnName("last_login");
            entity.Property(e => e.LastName)
                .HasMaxLength(50)
                .HasColumnName("last_name");
            entity.Property(e => e.Nickname)
                .HasMaxLength(50)
                .HasColumnName("nickname");
            entity.Property(e => e.Password).HasColumnName("password");
            entity.Property(e => e.PhoneNumber)
                .HasMaxLength(20)
                .HasColumnName("phone_number");
            entity.Property(e => e.RegisterDate)
                .HasDefaultValueSql("(getdate())")
                .HasColumnType("datetime")
                .HasColumnName("register_date");
            entity.Property(e => e.Username)
                .HasMaxLength(50)
                .HasColumnName("username");
        });

        modelBuilder.Entity<UserFollowCollection>(entity =>
        {
            entity.HasKey(e => new { e.UserId, e.CollectionId }).HasName("PK__User_Fol__0C830D537DC71D54");

            entity.ToTable("User_Follow_Collection");

            entity.Property(e => e.UserId).HasColumnName("user_id");
            entity.Property(e => e.CollectionId).HasColumnName("collection_id");
            entity.Property(e => e.FollowDate)
                .HasDefaultValueSql("(getdate())")
                .HasColumnType("datetime")
                .HasColumnName("follow_date");
            entity.Property(e => e.NotificationsEnabled)
                .HasDefaultValue(true)
                .HasColumnName("notifications_enabled");

            entity.HasOne(d => d.Collection).WithMany(p => p.UserFollowCollections)
                .HasForeignKey(d => d.CollectionId)
                .OnDelete(DeleteBehavior.ClientSetNull)
                .HasConstraintName("FK__User_Foll__colle__45F365D3");

            entity.HasOne(d => d.User).WithMany(p => p.UserFollowCollections)
                .HasForeignKey(d => d.UserId)
                .HasConstraintName("FK__User_Foll__user___44FF419A");
        });

        OnModelCreatingPartial(modelBuilder);
    }

    partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
}
