package org.example.frikollection_mobile_desktop.models.user

import kotlinx.serialization.Serializable
import org.example.frikollection_mobile_desktop.models.collection.CollectionDto

@Serializable
data class UserDto(
    val userId: String,
    val avatar: String,
    val nickname: String,
    val firstName: String,
    val lastName: String,
    val birthday: String? = null,
    val biography: String? = null,
    val ownCollections: List<CollectionDto> = emptyList()
)