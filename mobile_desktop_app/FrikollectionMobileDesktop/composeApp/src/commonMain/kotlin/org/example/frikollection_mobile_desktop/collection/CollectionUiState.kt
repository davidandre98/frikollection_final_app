package org.example.frikollection_mobile_desktop.collection

import org.example.frikollection_mobile_desktop.models.collection.CollectionDto
import org.example.frikollection_mobile_desktop.models.collection.CollectionStatsDto
import org.example.frikollection_mobile_desktop.models.collection.FollowedCollectionDto

data class CollectionUiState(
    val userCollections: List<CollectionDto> = emptyList(),
    val followedCollections: List<FollowedCollectionDto> = emptyList(),
    val selectedCollection: CollectionDto? = null,
    val statsMap: Map<String, CollectionStatsDto> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
)