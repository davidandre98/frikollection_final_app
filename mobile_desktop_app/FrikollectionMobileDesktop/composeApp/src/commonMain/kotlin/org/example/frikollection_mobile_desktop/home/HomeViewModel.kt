package org.example.frikollection_mobile_desktop.home

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.example.frikollection_mobile_desktop.api.ApiClient
import org.example.frikollection_mobile_desktop.models.product.ProductDto
import org.example.frikollection_mobile_desktop.models.ProductFilter

class HomeViewModel {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _carouselImages = MutableStateFlow<List<String>>(emptyList())
    val carouselImages: StateFlow<List<String>> = _carouselImages.asStateFlow()

    private val _productTypes = MutableStateFlow<List<String>>(emptyList())
    val productTypes: StateFlow<List<String>> = _productTypes.asStateFlow()

    init {
        loadProducts()
        loadProductTypes()
        loadCarouselImages()
    }

    private fun loadProducts() {
        _uiState.value = _uiState.value.copy(isLoading = true)

        scope.launch {
            try {
                val products: List<ProductDto> = ApiClient.getAllProducts()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    allProducts = products,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error loading products: ${e.message}"
                )
            }
        }
    }

    private fun loadProductTypes() {
        scope.launch {
            try {
                val types = ApiClient.getProductTypes()
                _productTypes.value = types
            } catch (e: Exception) {
                println("Error carregant tipus de producte: ${e.message}")
            }
        }
    }

    private fun loadCarouselImages() {
        scope.launch {
            try {
                val result = ApiClient.getCarouselImages()
                _carouselImages.value = result
            } catch (e: Exception) {
                println("Error carregant carroussel: ${e.message}")
            }
        }
    }

    fun onFilterChange(newFilter: ProductFilter) {
        _uiState.value = _uiState.value.copy(filter = newFilter)
    }

    fun onFilterRemoved(updated: ProductFilter) {
        _uiState.value = _uiState.value.copy(filter = updated)
    }

    fun onSortChanged(sort: SortOption) {
        _uiState.value = _uiState.value.copy(sort = sort)
    }

    fun setSelectedType(type: String?) {
        _uiState.value = _uiState.value.copy(selectedType = type)
    }

    fun setSelectedStatus(status: String?) {
        _uiState.value = _uiState.value.copy(selectedStatus = status)
    }
}