package org.example.frikollection_mobile_desktop.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductTypeDto(
    val typeName: String? = null,
    val hasExtension: Boolean? = null
)