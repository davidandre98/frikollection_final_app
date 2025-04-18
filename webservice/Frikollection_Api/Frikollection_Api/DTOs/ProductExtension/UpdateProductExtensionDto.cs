﻿using System.ComponentModel.DataAnnotations;

namespace Frikollection_Api.DTOs.ProductExtension
{
    public class UpdateProductExtensionDto
    {
        [Required]
        public Guid ProductExtensionId { get; set; }

        public int? Hp { get; set; }

        public List<string>? Types { get; set; }

        public string? EvolvesFrom { get; set; }

        public List<AbilityDto>? Abilities { get; set; }

        public List<AttackDto>? Attacks { get; set; }

        public int? ConvertedRetreatCost { get; set; }

        public string? Package { get; set; }

        public string? Expansion { get; set; }
    }
}
