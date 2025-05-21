package org.example.frikollection_mobile_desktop.models.collection

import kotlinx.serialization.Serializable

@Serializable
data class CollectionStatsDto(
    val collectionId: String,
    val totalProducts: Int,
    val totalValue: Double,
    val productTypes: Map<String, Int>
)