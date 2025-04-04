using Frikollection_Api.DTOs.Collection;
using Frikollection_Api.Infraestructure;
using Frikollection_Api.Models;
using Microsoft.EntityFrameworkCore;

namespace Frikollection_Api.Services
{
    public class CollectionService : ICollectionService
    {
        private readonly FrikollectionContext _context;

        public CollectionService(FrikollectionContext context)
        {
            _context = context;
        }

        public async Task<Collection> CreateCollectionAsync(CreateCollectionDto dto)
        {
            var nameExists = await _context.Collections
                .AnyAsync(c => c.UserId == dto.UserId && c.Name == dto.Name);

            if (nameExists)
                throw new InvalidOperationException("Ja existeix una col·lecció amb aquest nom per a aquest usuari.");

            var collection = new Collection
            {
                CollectionId = Guid.NewGuid(),
                Name = dto.Name,
                Private = dto.Private,
                UserId = dto.UserId,
                CreationDate = DateOnly.FromDateTime(DateTime.UtcNow)
            };

            _context.Collections.Add(collection);
            await _context.SaveChangesAsync();
            return collection;
        }

        public async Task<IEnumerable<Collection>> GetAllCollectionsAsync()
        {
            return await _context.Collections.ToListAsync();
        }

        public async Task<Collection?> GetCollectionByIdAsync(Guid id)
        {
            return await _context.Collections.FindAsync(id);
        }

        public async Task<bool> DeleteCollectionAsync(Guid id)
        {
            var collection = await _context.Collections.FindAsync(id);
            if (collection == null) return false;

            _context.Collections.Remove(collection);
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<Collection?> UpdateCollectionAsync(Guid id, UpdateCollectionDto dto)
        {
            var collection = await _context.Collections.FindAsync(id);
            if (collection == null)
                return null;

            var duplicateName = await _context.Collections
                .AnyAsync(c => c.UserId == collection.UserId && c.Name == dto.Name && c.CollectionId != id);

            if (duplicateName)
                throw new InvalidOperationException("Ja existeix una altra col·lecció amb aquest nom per a aquest usuari.");

            collection.Name = dto.Name;
            collection.Private = dto.Private;

            await _context.SaveChangesAsync();
            return collection;
        }

        public async Task<IEnumerable<UserCollectionDto>> GetUserCollectionsAsync(Guid userId, string? visibility = null)
        {
            var query = _context.Collections.AsQueryable();

            query = query.Where(c => c.UserId == userId);

            if (visibility == "public")
                query = query.Where(c => c.Private == false);

            else if (visibility == "private")
                query = query.Where(c => c.Private == true);

            var collections = await query
                .Select(c => new UserCollectionDto
                {
                    Name = c.Name,
                    Private = c.Private,
                    CreationDate = c.CreationDate
                })
                .ToListAsync();

            return collections;
        }

        public async Task<IEnumerable<UserCollectionDto>> GetPublicCollectionsByUserAsync(Guid userId)
        {
            var collections = await _context.Collections
                .Where(c => c.UserId == userId && c.Private == false)
                .Select(c => new UserCollectionDto
                {
                    Name = c.Name,
                    Private = c.Private,
                    CreationDate = c.CreationDate
                })
                .ToListAsync();

            return collections;
        }

        public async Task<bool> FollowCollectionAsync(Guid userId, Guid collectionId)
        {
            var collection = await _context.Collections
                .Include(c => c.User)
                .FirstOrDefaultAsync(c => c.CollectionId == collectionId);
            if (collection == null || collection.Private == true)
                return false;

            // Verifiquem si ja la segueix
            var alreadyFollowing = await _context.UserFollowCollections
                .AnyAsync(f => f.UserId == userId && f.CollectionId == collectionId);

            if (alreadyFollowing)
                return true;

            var follow = new UserFollowCollection
            {
                UserId = userId,
                CollectionId = collectionId,
                FollowDate = DateTime.UtcNow,
                NotificationsEnabled = true
            };

            _context.UserFollowCollections.Add(follow);
            await _context.SaveChangesAsync();
            await CreateFollowNotificationAsync(userId, collection);
            return true;
        }

        // Crea notificació a usuari propietari sobre el seguiment d'un altre usuari
        private async Task CreateFollowNotificationAsync(Guid followerUserId, Collection collection)
        {
            var recipient = collection.User;

            if (recipient == null || recipient.UserId == followerUserId)
                return;

            var notificationsEnabled = await _context.UserFollowCollections
                .Where(f => f.UserId == followerUserId && f.CollectionId == collection.CollectionId)
                .Select(f => f.NotificationsEnabled)
                .FirstOrDefaultAsync();

            if (notificationsEnabled == false)
                return;

            var follower = await _context.Users
                .FirstOrDefaultAsync(u => u.UserId == followerUserId);

            if (follower == null)
                return;

            var notification = new Notification
            {
                RecipientUserId = recipient.UserId,
                FollowerUserId = follower.UserId,
                CollectionId = collection.CollectionId,
                Message = $"{follower.Nickname} ha començat a seguir la teva col·lecció {collection.Name}.",
                CreatedAt = DateTime.UtcNow,
                IsRead = false
            };

            _context.Notifications.Add(notification);
            await _context.SaveChangesAsync();
        }

        public async Task<bool> UnfollowCollectionAsync(Guid userId, Guid collectionId)
        {
            var follow = await _context.UserFollowCollections
                .FirstOrDefaultAsync(f => f.UserId == userId && f.CollectionId == collectionId);

            if (follow == null)
                return false;

            _context.UserFollowCollections.Remove(follow);

            var notification = await _context.Notifications
                .FirstOrDefaultAsync(n =>
                    n.FollowerUserId == userId &&
                    n.CollectionId == collectionId);

            if (notification != null)
                _context.Notifications.Remove(notification);

            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<IEnumerable<UserCollectionDto>> GetFollowedCollectionsAsync(Guid userId)
        {
            var collections = await _context.UserFollowCollections
                .Where(f => f.UserId == userId && f.Collection.Private == false)
                .Select(f => new UserCollectionDto
                {
                    Name = f.Collection.Name,
                    Private = f.Collection.Private,
                    CreationDate = f.Collection.CreationDate
                })
                .ToListAsync();

            return collections;
        }

        public async Task<bool> AddProductToCollectionAsync(AddProductToCollectionDto dto)
        {
            var exists = await _context.CollectionProducts
                .AnyAsync(cp => cp.CollectionId == dto.CollectionId && cp.ProductId == dto.ProductId);
            if (exists) return false;

            _context.CollectionProducts.Add(new CollectionProduct
            {
                CollectionId = dto.CollectionId,
                ProductId = dto.ProductId
            });

            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> RemoveProductFromCollectionAsync(Guid collectionId, Guid productId)
        {
            var entry = await _context.CollectionProducts.FindAsync(collectionId, productId);
            if (entry == null) return false;

            _context.CollectionProducts.Remove(entry);
            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<IEnumerable<Product>> GetProductsInCollectionAsync(Guid collectionId)
        {
            return await _context.CollectionProducts
                .Where(cp => cp.CollectionId == collectionId)
                .Include(cp => cp.Product)
                    .ThenInclude(p => p.ProductType)
                .Include(cp => cp.Product)
                    .ThenInclude(p => p.ProductExtension)
                .Include(cp => cp.Product)
                    .ThenInclude(p => p.Tags)
                .Select(cp => cp.Product)
                .ToListAsync();
        }

        public async Task<CollectionStatsDto> GetCollectionStatsAsync(Guid collectionId)
        {
            var products = await _context.CollectionProducts
                .Where(cp => cp.CollectionId == collectionId)
                .Include(cp => cp.Product)
                    .ThenInclude(p => p.ProductType)
                .Select(cp => cp.Product)
                .ToListAsync();

            var stats = new CollectionStatsDto
            {
                CollectionId = collectionId,
                TotalProducts = products.Count,
                TotalValue = products.Sum(p => p.Value ?? 0),
                ProductTypes = products
                    .GroupBy(p => p.ProductType?.TypeName ?? "Sense Tipus")
                    .ToDictionary(g => g.Key, g => g.Count())
            };

            return stats;
        }
    }
}
