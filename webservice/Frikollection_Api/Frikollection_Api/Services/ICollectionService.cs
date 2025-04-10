using Frikollection_Api.DTOs.Collection;
using Frikollection_Api.Models;

namespace Frikollection_Api.Services
{
    public interface ICollectionService
    {
        Task<Collection> CreateCollectionAsync(CreateCollectionDto dto);
        Task<IEnumerable<Collection>> GetAllCollectionsAsync();
        Task<Collection?> GetCollectionByIdAsync(Guid id);
        Task<bool> DeleteCollectionAsync(Guid id);
        Task<Collection?> UpdateCollectionAsync(Guid id, UpdateCollectionDto dto);
        Task<IEnumerable<UserCollectionDto>> GetUserCollectionsAsync(Guid userId, string? visibility = null);
        Task<IEnumerable<UserCollectionDto>> GetPublicCollectionsByUserAsync(Guid userId);
        Task<bool> FollowCollectionAsync(Guid userId, Guid collectionId);
        Task<bool> UnfollowCollectionAsync(Guid userId, Guid collectionId);
        Task<IEnumerable<UserCollectionDto>> GetFollowedCollectionsAsync(Guid userId);
        Task<bool> AddProductToCollectionAsync(AddProductToCollectionDto dto);
        Task<bool> RemoveProductFromCollectionAsync(Guid collectionId, Guid productId);
        Task<IEnumerable<Product>> GetProductsInCollectionAsync(Guid collectionId);
        Task<CollectionStatsDto> GetCollectionStatsAsync(Guid collectionId);
        CollectionPreviewDto ToDto(Collection collection);
    }
}
