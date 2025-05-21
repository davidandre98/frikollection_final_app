package org.example.frikollection_mobile_desktop.models.collection

import kotlinx.serialization.Serializable

@Serializable
data class FollowedCollectionDto(
    val collectionId: String,
    val name: String,
    val ownerNickname: String,
    val followDate: String
)