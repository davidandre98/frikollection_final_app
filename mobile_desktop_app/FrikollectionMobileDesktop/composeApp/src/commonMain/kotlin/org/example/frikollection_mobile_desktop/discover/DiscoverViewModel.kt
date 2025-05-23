package org.example.frikollection_mobile_desktop.discover

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.frikollection_mobile_desktop.api.ApiClient
import org.example.frikollection_mobile_desktop.api.AppConfig

class DiscoverViewModel {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _uiState = MutableStateFlow(DiscoverUiState())
    val uiState: StateFlow<DiscoverUiState> = _uiState.asStateFlow()

    fun loadDiscoverData() {
        val currentUserId = AppConfig.loggedUserId ?: return

        _uiState.value = _uiState.value.copy(isLoading = true)
        scope.launch {
            try {
                val allUsers = ApiClient.getAllUsers()
                val allCollections = ApiClient.getAllCollections()

                val otherUsers = allUsers.filter { it.userId != currentUserId }
                val otherCollections = allCollections.filterNot { collection ->
                    collection.user?.username == AppConfig.loggedUsername
                }

                _uiState.value = DiscoverUiState(
                    otherUsers = otherUsers,
                    otherCollections = otherCollections,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = DiscoverUiState(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
}