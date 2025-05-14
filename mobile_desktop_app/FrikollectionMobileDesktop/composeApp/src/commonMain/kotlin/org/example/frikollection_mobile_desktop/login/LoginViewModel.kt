package org.example.frikollection_mobile_desktop.login

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.frikollection_mobile_desktop.api.ApiClient
import org.example.frikollection_mobile_desktop.ui.errors.ErrorParser
import org.example.frikollection_mobile_desktop.state.AppState

class LoginViewModel {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onIdentifierChange(newUsername: String) {
        _uiState.value = _uiState.value.copy(identifier = newUsername)
    }

    fun onPasswordChange(newPassword: String) {
        _uiState.value = _uiState.value.copy(password = newPassword)
    }

    fun onLogin(onSuccess: (String) -> Unit) {
        val state = _uiState.value

        if (state.identifier.isBlank() || state.password.isBlank()) {
            _uiState.value = state.copy(error = "Please fill in all fields")
            return
        }

        _uiState.value = state.copy(isLoading = true, error = null)

        scope.launch {
            try {
                val response = ApiClient.login(state.identifier, state.password)
                AppState.loginSuccess(response.userId)
                _uiState.value = state.copy(isSuccess = true, isLoading = false)
                onSuccess(response.userId)
            } catch (e: Exception) {
                val errorMessage = ErrorParser.extract(e.message)
                _uiState.value = state.copy(error = errorMessage, isLoading = false)
            }
        }
    }
}