package org.example.frikollection_mobile_desktop.search

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.frikollection_mobile_desktop.api.ApiClient
import org.example.frikollection_mobile_desktop.models.ProductFilter

class SearchViewModel {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        scope.launch {
            try {
                val products = ApiClient.getAllProducts()
                _uiState.value = _uiState.value.copy(allProducts = products)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error loading products: ${e.message}"
                )
            }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun clearQuery() {
        _uiState.value = _uiState.value.copy(query = "")
    }

    fun onFilterChange(newFilter: ProductFilter) {
        _uiState.value = _uiState.value.copy(filter = newFilter)
    }

    fun onFilterRemoved(update: ProductFilter) {
        _uiState.value = _uiState.value.copy(filter = update)
    }
}