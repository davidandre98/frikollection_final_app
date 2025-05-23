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
import org.example.frikollection_mobile_desktop.home.SortOption
import org.example.frikollection_mobile_desktop.models.ProductFilter
import org.example.frikollection_mobile_desktop.models.collection.CollectionDto
import org.example.frikollection_mobile_desktop.models.collection.CollectionPreviewDto
import org.example.frikollection_mobile_desktop.models.collection.FollowedCollectionDto
import org.example.frikollection_mobile_desktop.models.collection.UserCollectionDto
import org.example.frikollection_mobile_desktop.models.product.ProductDto

class CollectionViewModel : ViewModel(){
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _uiState = MutableStateFlow(CollectionUiState())
    val uiState: StateFlow<CollectionUiState> = _uiState.asStateFlow()

    init {
        loadCollections()
    }

    fun loadAllProducts(onLoaded: () -> Unit = {}) {
        scope.launch {
            try {
                val products = ApiClient.getAllProducts()
                _uiState.value = _uiState.value.copy(allProducts = products)
                onLoaded()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
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
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun selectUserCollection(collection: UserCollectionDto) {
        _uiState.value = _uiState.value.copy(
            selectedUserCollection = collection,
            selectedFollowedCollection = null,
            isViewingFollowed = false,
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

    fun selectFollowedCollection(collection: FollowedCollectionDto) {
        _uiState.value = _uiState.value.copy(
            selectedFollowedCollection = collection,
            selectedUserCollection = null,
            isViewingFollowed = true,
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
        _uiState.value = _uiState.value.copy(
            selectedUserCollection = null,
            selectedFollowedCollection = null,
            isViewingFollowed = false
        )
    }

    fun createCollection(name: String, isPrivate: Boolean) {
        val userId = AppConfig.loggedUserId ?: return
        scope.launch {
            try {
                ApiClient.createCollection(userId, name, isPrivate)
                loadCollections()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun updateCollection(collectionId: String, name: String, private: Boolean) {
        scope.launch {
            try {
                ApiClient.updateCollection(collectionId, name, private)
                loadCollections()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
        }
    }

    fun deleteCollection(collectionId: String) {
        scope.launch {
            try {
                ApiClient.deleteCollection(collectionId)
                loadCollections()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
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

    fun toggleProductToAdd(product: ProductDto) {
        val current = _uiState.value.productsToAdd.toMutableList()
        if (current.any { it.productId == product.productId }) {
            current.removeAll { it.productId == product.productId }
        } else {
            current.add(product)
        }
        _uiState.value = _uiState.value.copy(productsToAdd = current)
    }

    fun addProductsToCollection(collectionId: String, productIds: List<String>, onResult: (List<String>, List<String>) -> Unit) {
        scope.launch {
            try {
                val (added, alreadyExists) = ApiClient.addProductsToCollection(collectionId, productIds)
                loadCollections()
                onResult(added, alreadyExists)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun toggleProductToRemove(product: ProductDto) {
        val current = _uiState.value.productsToRemove.toMutableList()
        if (current.any { it.productId == product.productId }) {
            current.removeAll { it.productId == product.productId }
        } else {
            current.add(product)
        }
        _uiState.value = _uiState.value.copy(productsToRemove = current)
    }

    fun removeProductFromCollection(collectionId: String, productId: String, onSuccess: () -> Unit) {
        scope.launch {
            try {
                val success = ApiClient.removeProductFromCollection(collectionId, productId)
                if (success) {
                    loadCollections()
                    onSuccess()
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
            }
        }
    }

    fun clearSelectedProducts() {
        _uiState.value = _uiState.value.copy(productsToAdd = emptyList(), productsToRemove = emptyList())
    }

    fun refreshSelectedCollection(onFinished: () -> Unit = {}) {
        val selected = _uiState.value.selectedUserCollection ?: return
        scope.launch {
            try {
                val updatedCollections = ApiClient.getUserCollections(AppConfig.loggedUserId ?: return@launch)
                val updated = updatedCollections.find { it.collectionId == selected.collectionId }
                if (updated != null) {
                    _uiState.value = _uiState.value.copy(selectedUserCollection = updated)
                }
                onFinished()
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
                onFinished()
            }
        }
    }

    fun toggleProductInWishlist(product: ProductDto, onAdded: (Boolean) -> Unit) {
        scope.launch {
            try {
                val wishlist = _uiState.value.userCollections.find { it.name == "My Wishlist" }
                    ?: return@launch

                val productInWishlist = wishlist.products.any { it.productId == product.productId }
                if (!productInWishlist) {
                    val (added, _) = ApiClient.addProductsToCollection(wishlist.collectionId, listOf(product.productId))
                    loadCollections()
                    onAdded(true)
                } else {
                    onAdded(true)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message)
                onAdded(false)
            }
        }
    }

    fun isProductInWishlist(productId: String): Boolean {
        val wishlist = _uiState.value.userCollections.find { it.name == "My Wishlist" }
        return wishlist?.products?.any { it.productId == productId } == true
    }

    fun selectPublicCollection(collection: CollectionPreviewDto) {
        _uiState.value = _uiState.value.copy(
            selectedUserCollection = null,
            selectedFollowedCollection = null,
            isViewingFollowed = false,
            publicPreviewCollection = collection
        )
    }
}