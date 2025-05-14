package org.example.frikollection_mobile_desktop.models

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val productId: String,
    val name: String,
    val supertype: String? = null,
    val subtype: String? = null,
    val release: String? = null, // Format "YYYY-MM-DD"
    val status: String? = null,
    val itemNumber: Int? = null,
    val license: String? = null,
    val width: Double? = null,
    val height: Double? = null,
    val value: Double? = null,
    val smallPicture: String? = null,
    val bigPicture: String? = null,
    val productType: ProductTypeDto? = null,
    val productExtension: ProductExtensionDto? = null,
    val tags: List<TagDto> = emptyList()
)