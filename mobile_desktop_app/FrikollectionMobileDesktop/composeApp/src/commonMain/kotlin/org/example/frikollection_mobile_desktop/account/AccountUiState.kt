package org.example.frikollection_mobile_desktop.account

import org.example.frikollection_mobile_desktop.models.user.UserProfileDto


data class AccountUiState(
    val profile: UserProfileDto? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)