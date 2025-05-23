package org.example.frikollection_mobile_desktop.models.collection

import kotlinx.serialization.Serializable

@Serializable
data class AddProductToCollectionDto(
    val collectionId: String,
    val productIds: List<String>
)