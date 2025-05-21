package org.example.frikollection_mobile_desktop.models.collection

import kotlinx.serialization.Serializable
import org.example.frikollection_mobile_desktop.models.product.ProductDto

@Serializable
data class UserCollectionDto(
    val collectionId: String,
    val name: String,
    val private: Boolean,
    val creationDate: String,
    val products: List<ProductDto>
)