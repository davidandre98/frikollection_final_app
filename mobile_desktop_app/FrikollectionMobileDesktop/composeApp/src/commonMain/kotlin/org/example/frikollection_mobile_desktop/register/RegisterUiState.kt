package org.example.frikollection_mobile_desktop.register

data class RegisterUiState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val birthday: String = "",
    val acceptTerms: Boolean = false,
    val acceptPrivacy: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)