package org.example.frikollection_mobile_desktop.discover

import org.example.frikollection_mobile_desktop.models.collection.CollectionPreviewDto
import org.example.frikollection_mobile_desktop.models.user.UserDto

data class DiscoverUiState(
    val otherUsers: List<UserDto> = emptyList(),
    val otherCollections: List<CollectionPreviewDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)