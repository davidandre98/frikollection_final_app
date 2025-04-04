using System.ComponentModel.DataAnnotations;

namespace Frikollection_Api.DTOs.Tag
{
    public class UpdateTagDto
    {
        [Required]
        public Guid TagId { get; set; }

        [Required]
        [StringLength(50)]
        public string Name { get; set; } = null!;

        public string? TagImageUrl { get; set; }
    }
}
