﻿using System.ComponentModel.DataAnnotations;

namespace Frikollection_Api.DTOs.Collection
{
    public class CreateCollectionDto
    {
        [Required]
        [MinLength(1)]
        public string Name { get; set; }

        public bool Private { get; set; } = false;
        
        [Required]
        public Guid UserId { get; set; }  // A quin usuari pertany
    }
}
