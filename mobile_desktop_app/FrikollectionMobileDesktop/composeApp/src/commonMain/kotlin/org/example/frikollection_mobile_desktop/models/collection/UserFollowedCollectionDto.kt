package org.example.frikollection_mobile_desktop.models.collection

import kotlinx.serialization.Serializable

@Serializable
data class UserFollowedCollectionDto(
    val userId: String,
    val username: String,
    val nickname: String,
    val followedCollections: List<FollowedCollectionDto>
)