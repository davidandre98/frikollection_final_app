package org.example.frikollection_mobile_desktop.models

import kotlinx.serialization.Serializable

@Serializable
data class TagDto(
    val name: String? = null,
    val tagImage: String? = null
)