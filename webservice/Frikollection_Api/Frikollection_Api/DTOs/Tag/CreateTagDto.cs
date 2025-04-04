using System.ComponentModel.DataAnnotations;

namespace Frikollection_Api.DTOs.Tag
{
    public class CreateTagDto
    {
        [Required]
        [StringLength(50)]
        public string Name { get; set; } = null!;

        public string? TagImageUrl { get; set; }
    }
}
