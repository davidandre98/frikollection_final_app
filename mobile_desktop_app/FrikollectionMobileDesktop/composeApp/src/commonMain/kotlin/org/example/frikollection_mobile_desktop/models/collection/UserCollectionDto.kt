package org.example.frikollection_mobile_desktop.models.collection

import kotlinx.serialization.Serializable
import org.example.frikollection_mobile_desktop.models.product.ProductDto

@Serializable
data class UserCollectionDto(
    val collectionId: String,
    val name: String,
    val isPrivate: Boolean,
    val createdAt: String,
    val products: List<ProductDto>
)