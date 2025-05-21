package org.example.frikollection_mobile_desktop.models.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductTypeDto(
    val typeName: String? = null,
    val hasExtension: Boolean? = null
)