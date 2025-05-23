package org.example.frikollection_mobile_desktop.models.collection

import kotlinx.serialization.Serializable
import org.example.frikollection_mobile_desktop.models.product.ProductDto

@Serializable
data class CollectionPreviewDto(
    val name: String,
    val private: Boolean? = null,
    val creationDate: String? = null,
    val user: UserPreviewDto? = null,
    val products: List<ProductDto> = emptyList()
)

@Serializable
data class UserPreviewDto(
    val username: String
)