using Frikollection_Api.DTOs.Tag;
using Frikollection_Api.Models;

namespace Frikollection_Api.Services
{
    public interface ITagService
    {
        Task<Tag> CreateAsync(CreateTagDto dto);
        Task<IEnumerable<Tag>> GetAllAsync();
        Task<Tag?> GetByIdAsync(Guid id);
        Task<bool> UpdateAsync(UpdateTagDto dto);
        Task<bool> DeleteAsync(Guid id);
        TagDto ToDto(Tag tag);
    }
}
