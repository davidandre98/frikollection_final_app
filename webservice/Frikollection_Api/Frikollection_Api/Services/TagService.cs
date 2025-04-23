using Frikollection_Api.DTOs.Tag;
using Frikollection_Api.Infraestructure;
using Frikollection_Api.Models;
using Microsoft.EntityFrameworkCore;

namespace Frikollection_Api.Services
{
    public class TagService : ITagService
    {
        private readonly FrikollectionContext _context;

        public TagService(FrikollectionContext context)
        {
            _context = context;
        }

        public async Task<IEnumerable<Tag>> GetAllAsync()
        {
            return await _context.Tags.ToListAsync();
        }

        public async Task<Tag?> GetByIdAsync(Guid id)
        {
            return await _context.Tags.FindAsync(id);
        }

        public async Task<Tag> CreateAsync(CreateTagDto dto)
        {
            var tag = new Tag
            {
                TagId = Guid.NewGuid(),
                Name = dto.Name,
                TagImage = dto.TagImageUrl
            };

            _context.Tags.Add(tag);
            await _context.SaveChangesAsync();
            return tag;
        }

        public async Task<bool> UpdateAsync(UpdateTagDto dto)
        {
            var tag = await _context.Tags.FindAsync(dto.TagId);
            if (tag == null) return false;

            tag.Name = dto.Name;
            tag.TagImage = dto.TagImageUrl;

            await _context.SaveChangesAsync();
            return true;
        }

        public async Task<bool> DeleteAsync(Guid id)
        {
            var tag = await _context.Tags
                .Include(t => t.Products)
                .FirstOrDefaultAsync(t => t.TagId == id);

            if (tag == null || tag.Products.Any())
                return false; // No eliminar si està en ús

            _context.Tags.Remove(tag);
            await _context.SaveChangesAsync();
            return true;
        }

        public TagDto ToDto(Tag tag)
        {
            return new TagDto
            {
                Name = tag.Name,
                TagImage = tag.TagImage
            };
        }
    }
}
