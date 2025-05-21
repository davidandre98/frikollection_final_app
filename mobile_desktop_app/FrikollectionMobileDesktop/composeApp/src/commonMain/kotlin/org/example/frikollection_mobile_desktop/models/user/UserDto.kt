package org.example.frikollection_mobile_desktop.models.user

import kotlinx.serialization.Serializable
import org.example.frikollection_mobile_desktop.models.collection.FollowedCollectionDto

@Serializable
data class UserDto(
    val userId: String,
    val username: String,
    val nickname: String,
    val followedCollections: List<FollowedCollectionDto>
)