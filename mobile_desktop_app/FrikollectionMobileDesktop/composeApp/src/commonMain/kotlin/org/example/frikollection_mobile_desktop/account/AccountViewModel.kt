package org.example.frikollection_mobile_desktop.account

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.frikollection_mobile_desktop.api.ApiClient
import org.example.frikollection_mobile_desktop.api.AppConfig
import org.example.frikollection_mobile_desktop.models.user.UpdateUserDto
import java.io.File

class AccountViewModel : ViewModel() {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _uiState = MutableStateFlow(AccountUiState())
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    fun loadUserProfile() {
        val userId = AppConfig.loggedUserId ?: return

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        scope.launch {
            try {
                val profile = ApiClient.getUserProfile(userId)
                _uiState.value = _uiState.value.copy(profile = profile, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun updateAvatar(fileBytes: ByteArray, fileName: String, onComplete: () -> Unit = {}) {
        val userId = AppConfig.loggedUserId ?: return

        scope.launch {
            try {
                val avatarUrl = ApiClient.uploadAvatar(userId, fileBytes, fileName)

                val updatedProfile = _uiState.value.profile?.copy(avatar = avatarUrl)
                _uiState.value = _uiState.value.copy(profile = updatedProfile)

                onComplete()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateBiography(newBio: String) {
        val userId = AppConfig.loggedUserId ?: return
        val currentProfile = _uiState.value.profile ?: return

        scope.launch {
            try {
                ApiClient.updateUser(
                    userId,
                    UpdateUserDto(biography = newBio)
                )

                // Actualitza localment l'estat
                val updatedProfile = currentProfile.copy(biography = newBio)
                _uiState.value = _uiState.value.copy(profile = updatedProfile)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun updateUserInfo(dto: UpdateUserDto, onComplete: () -> Unit = {}) {
        val userId = AppConfig.loggedUserId ?: return

        scope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val updated = ApiClient.updateUser(userId, dto)
                _uiState.value = _uiState.value.copy(profile = updated, isLoading = false)
                onComplete()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }
}