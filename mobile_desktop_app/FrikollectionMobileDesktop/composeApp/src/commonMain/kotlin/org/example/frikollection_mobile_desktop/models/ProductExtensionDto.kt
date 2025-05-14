package org.example.frikollection_mobile_desktop.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductExtensionDto(
    val hp: Int? = null,
    val pokemonTypes: String? = null,
    val evolvesFrom: String? = null,
    val abilities: List<AbilityDto>?,
    val attacks: List<AttackDto>?,
    val convertedRetreatCost: Int? = null,
    @SerialName("package")
    val packageName: String? = null,
    val expansion: String? = null
)