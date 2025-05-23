package org.example.frikollection_mobile_desktop.models.collection

import kotlinx.serialization.Serializable
import org.example.frikollection_mobile_desktop.models.product.ProductDto

@Serializable
data class FollowedCollectionDto(
    val collectionId: String,
    val name: String,
    val private: Boolean,
    val ownerNickname: String,
    val followDate: String,
    val products: List<ProductDto>
)