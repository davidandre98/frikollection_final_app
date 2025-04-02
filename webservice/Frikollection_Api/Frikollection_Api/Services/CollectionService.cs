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
    }
}
