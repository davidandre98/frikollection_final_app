package org.example.frikollection_mobile_desktop.models.collection

import kotlinx.serialization.Serializable

@Serializable
data class CollectionDto(
    val collectionId: String,
    val name: String,
    val private: Boolean,
    val creationDate: String,
    val totalProducts: Int
)