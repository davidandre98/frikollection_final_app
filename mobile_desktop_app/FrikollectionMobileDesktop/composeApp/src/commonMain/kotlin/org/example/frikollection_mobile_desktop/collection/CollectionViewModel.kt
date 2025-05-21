package org.example.frikollection_mobile_desktop.collection

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.frikollection_mobile_desktop.api.ApiClient
import org.example.frikollection_mobile_desktop.api.AppConfig
import org.example.frikollection_mobile_desktop.models.collection.CollectionDto

class CollectionViewModel {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _uiState = MutableStateFlow(CollectionUiState())
    val uiState: StateFlow<CollectionUiState> = _uiState.asStateFlow()

    init {
        loadCollections()
    }

    fun loadCollections() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        scope.launch {
            try {
                val userId = AppConfig.loggedUserId ?: return@launch
                val userCollections = ApiClient.getUserCollections(userId)
                val followedCollections = ApiClient.getFollowedCollections(userId)

                // Per cada col·lecció, carreguem stats i construïm un map
                val statsMap = userCollections.associate { collection ->
                    collection.collectionId to ApiClient.getCollectionStats(collection.collectionId)
                }.toMutableMap()

                _uiState.value = _uiState.value.copy(
                    userCollections = userCollections,
                    followedCollections = followedCollections,
                    statsMap = statsMap,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun selectCollection(collection: CollectionDto) {
        _uiState.value = _uiState.value.copy(
            selectedCollection = collection,
            isLoading = true,
            error = null
        )

        scope.launch {
            try {
                val stats = ApiClient.getCollectionStats(collection.collectionId)
                val updatedMap = _uiState.value.statsMap.toMutableMap().apply {
                    put(collection.collectionId, stats)
                }
                _uiState.value = _uiState.value.copy(statsMap = updatedMap, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun clearSelectedCollection() {
        _uiState.value = _uiState.value.copy(selectedCollection = null)
    }
}