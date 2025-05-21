package org.example.frikollection_mobile_desktop.search

import org.example.frikollection_mobile_desktop.models.product.ProductDto
import org.example.frikollection_mobile_desktop.models.ProductFilter

data class SearchUiState(
    val allProducts: List<ProductDto> = emptyList(),
    val query: String = "",
    val filter: ProductFilter = ProductFilter(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val filteredProducts: List<ProductDto>
        get() = allProducts.filter { product ->
            val matchesSearch = query.isBlank() || product.name.contains(query, ignoreCase = true)
                    || product.license?.contains(query, ignoreCase = true) == true

            val matchesFilter = (filter.productType == null || product.productType?.typeName.equals(filter.productType, ignoreCase = true)) &&
                    (filter.supertype == null || product.supertype.equals(filter.supertype, ignoreCase = true)) &&
                    (filter.subtype == null || product.subtype.equals(filter.subtype, ignoreCase = true)) &&
                    (filter.status == null || product.status.equals(filter.status, ignoreCase = true)) &&
                    (filter.license == null || product.license.equals(filter.license, ignoreCase = true)) &&
                    (filter.tags.isEmpty() || filter.tags.all { tagName ->
                        product.tags.any { it.name.equals(tagName, ignoreCase = true) }
                    })

            matchesSearch && matchesFilter
        }.sortedByDescending { it.release }
}

