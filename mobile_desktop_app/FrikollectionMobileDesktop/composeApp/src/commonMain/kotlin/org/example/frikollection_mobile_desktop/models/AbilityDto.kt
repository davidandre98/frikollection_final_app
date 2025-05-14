package org.example.frikollection_mobile_desktop.models

import kotlinx.serialization.Serializable

@Serializable
data class AbilityDto(
    val name: String,
    val text: String,
    val type: String
)