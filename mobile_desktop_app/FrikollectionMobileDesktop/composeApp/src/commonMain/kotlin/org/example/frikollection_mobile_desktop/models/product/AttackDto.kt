package org.example.frikollection_mobile_desktop.models.product

import kotlinx.serialization.Serializable

@Serializable
data class AttackDto(
    val name: String,
    val cost: List<String>?,
    val convertedEnergyCost: Int,
    val damage: String,
    val text: String
)