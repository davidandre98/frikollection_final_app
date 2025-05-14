package org.example.frikollection_mobile_desktop.register

import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val birthday: String
)