using System.ComponentModel.DataAnnotations;

namespace Frikollection_Api.DTOs.Collection
{
    public class UpdateCollectionDto
    {
        [Required]
        [MinLength(1)]
        public string Name { get; set; }

        public bool Private { get; set; }
    }
}
