package org.example.frikollection_mobile_desktop.collection

import org.example.frikollection_mobile_desktop.home.SortOption
import org.example.frikollection_mobile_desktop.models.ProductFilter
import org.example.frikollection_mobile_desktop.models.collection.CollectionDto
import org.example.frikollection_mobile_desktop.models.collection.CollectionPreviewDto
import org.example.frikollection_mobile_desktop.models.collection.CollectionStatsDto
import org.example.frikollection_mobile_desktop.models.collection.FollowedCollectionDto
import org.example.frikollection_mobile_desktop.models.collection.UserCollectionDto
import org.example.frikollection_mobile_desktop.models.product.ProductDto

data class CollectionUiState(
    val userCollections: List<UserCollectionDto> = emptyList(),
    val followedCollections: List<FollowedCollectionDto> = emptyList(),
    val selectedUserCollection: UserCollectionDto? = null,
    val selectedFollowedCollection: FollowedCollectionDto? = null,
    val publicPreviewCollection: CollectionPreviewDto? = null,
    val allProducts: List<ProductDto> = emptyList(),
    val productsToAdd: List<ProductDto> = emptyList(),
    val productsToRemove: List<ProductDto> = emptyList(),
    val isViewingFollowed: Boolean = false,
    val query: String = "",
    val filter: ProductFilter = ProductFilter(),
    val sort: SortOption = SortOption.NameAsc,
    val statsMap: Map<String, CollectionStatsDto> = emptyMap(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val filteredProducts: List<ProductDto>
        get() {
            val products = if (isViewingFollowed) {
                selectedFollowedCollection?.products
            } else {
                selectedUserCollection?.products
            } ?: return emptyList()

            return products
                .asSequence()
                .filter { product ->
                    val matchesSearch = query.isBlank() || product.name.contains(query, ignoreCase = true)
                    val matchesFilter =
                        (filter.productType == null || product.productType?.typeName.equals(filter.productType, ignoreCase = true)) &&
                                (filter.supertype == null || product.supertype.equals(filter.supertype, ignoreCase = true)) &&
                                (filter.subtype == null || product.subtype.equals(filter.subtype, ignoreCase = true)) &&
                                (filter.status == null || product.status.equals(filter.status, ignoreCase = true)) &&
                                (filter.license == null || product.license.equals(filter.license, ignoreCase = true)) &&
                                (filter.tags.isEmpty() || filter.tags.all { tag ->
                                    product.tags.any { it.name.equals(tag, ignoreCase = true) }
                                })
                    matchesSearch && matchesFilter
                }
                .sortedWith(
                    when (sort) {
                        SortOption.NameAsc -> compareBy { it.name }
                        SortOption.NameDesc -> compareByDescending { it.name }
                        SortOption.ReleaseAsc -> compareBy { it.release }
                        SortOption.ReleaseDesc -> compareByDescending { it.release }
                        SortOption.ValueAsc -> compareBy { it.value ?: 0.0 }
                        SortOption.ValueDesc -> compareByDescending { it.value ?: 0.0 }
                    }
                )
                .toList()
        }
}