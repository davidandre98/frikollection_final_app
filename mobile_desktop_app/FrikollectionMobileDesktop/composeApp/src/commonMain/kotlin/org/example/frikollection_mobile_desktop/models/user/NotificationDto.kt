package org.example.frikollection_mobile_desktop.models.user

import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    val notificationId: String,
    val message: String,
    val followerNickname: String,
    val collectionName: String,
    val createdAt: String,
    val isRead: Boolean
)