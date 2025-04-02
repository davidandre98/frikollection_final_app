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
    }
}
