package org.example.frikollection_mobile_desktop.register

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.frikollection_mobile_desktop.api.ApiClient
import org.example.frikollection_mobile_desktop.ui.errors.ErrorParser

class RegisterViewModel {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _state = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _state.asStateFlow()

    fun update(transform: (RegisterUiState) -> RegisterUiState) {
        _state.value = transform(_state.value)
    }

    fun clearState() {
        update {
            RegisterUiState()
        }
    }

    fun submit(onSuccess: () -> Unit) {
        val s = _state.value

        val error = when {
            s.username.isBlank() || s.email.isBlank() || s.password.isBlank() || s.confirmPassword.isBlank()
                    || s.firstName.isBlank() || s.lastName.isBlank() || s.birthday.isBlank() ->
                "Please fill in all fields."

            s.password != s.confirmPassword ->
                "Passwords do not match."

            !s.acceptTerms || !s.acceptPrivacy ->
                "You must accept the terms and privacy policy."

            else -> null
        }

        if (error != null) {
            _state.value = s.copy(error = error)
            return
        }

        _state.value = s.copy(isLoading = true, error = null)

        scope.launch {
            try {
                val req = RegisterUserRequest(
                    username = s.username,
                    email = s.email,
                    password = s.password,
                    firstName = s.firstName,
                    lastName = s.lastName,
                    birthday = s.birthday
                )

                ApiClient.registerUser(req)
                _state.value = s.copy(isLoading = false, success = true)
                onSuccess()
            } catch (e: Exception) {
                val errorMessage = ErrorParser.extract(e.message)
                _state.value = s.copy(isLoading = false, error = errorMessage)
            }
        }
    }
}