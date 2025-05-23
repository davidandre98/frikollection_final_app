package org.example.frikollection_mobile_desktop.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val userId: String,
    val username: String,
    val password: String,
    val email: String,
    val avatar: String?,
    val nickname: String,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val birthday: String?,
    val biography: String?,
    val registerDate: String?,
    val lastLogin: String?
)