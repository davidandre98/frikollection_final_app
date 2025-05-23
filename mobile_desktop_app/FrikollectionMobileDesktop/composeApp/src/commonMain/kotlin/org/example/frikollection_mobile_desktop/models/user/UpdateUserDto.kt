package org.example.frikollection_mobile_desktop.models.user

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserDto(
    val avatar: String? = null,
    val nickname: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
    val birthday: String? = null,
    val biography: String? = null,
    val username: String? = null,
    val currentPassword: String? = null,
    val newPassword: String? = null
)