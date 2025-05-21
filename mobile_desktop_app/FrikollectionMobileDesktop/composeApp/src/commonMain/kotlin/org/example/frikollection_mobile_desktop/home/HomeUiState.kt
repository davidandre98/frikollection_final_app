package org.example.frikollection_mobile_desktop.home

import org.example.frikollection_mobile_desktop.models.product.ProductDto
import org.example.frikollection_mobile_desktop.models.ProductFilter

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val allProducts: List<ProductDto> = emptyList(),
    val filter: ProductFilter = ProductFilter(),
    val selectedType: String? = null,
    val selectedStatus: String? = null,
    val sort: SortOption = SortOption.NameAsc
) {
    val filteredProducts: List<ProductDto>
        get() {
            val base = when (selectedType?.lowercase()) {
                "funko" -> allProducts.filter { it.productType?.typeName.equals("Funko", ignoreCase = true) }
                "tcg" -> allProducts.filter { it.productType?.typeName.equals("TCG", ignoreCase = true) }
                "figure" -> allProducts.filter { it.productType?.typeName.equals("Figure", ignoreCase = true) }
                "all catalog" -> allProducts
                else -> allProducts
            }

            val filtered = base.filter { product ->
                (filter.productType == null || product.productType?.typeName.equals(filter.productType, ignoreCase = true)) &&
                        (filter.supertype == null || product.supertype.equals(filter.supertype, ignoreCase = true)) &&
                        (filter.subtype == null || product.subtype.equals(filter.subtype, ignoreCase = true)) &&
                        (filter.status == null || product.status.equals(filter.status, ignoreCase = true)) &&
                        (filter.license == null || product.license.equals(filter.license, ignoreCase = true)) &&
                        (filter.tags.isEmpty() || filter.tags.all { tag ->
                            product.tags.any { it.name.equals(tag, ignoreCase = true) }
                        }) &&
                        (selectedStatus == null || product.status.equals(selectedStatus, ignoreCase = true))
            }

            return when (sort) {
                SortOption.NameAsc -> filtered.sortedBy { it.name }
                SortOption.NameDesc -> filtered.sortedByDescending { it.name }
                SortOption.ReleaseAsc -> filtered.sortedBy { it.release ?: "" }
                SortOption.ReleaseDesc -> filtered.sortedByDescending { it.release ?: "" }
                SortOption.ValueAsc -> filtered.sortedBy { it.value ?: 0.0 }
                SortOption.ValueDesc -> filtered.sortedByDescending { it.value ?: 0.0 }
            }
        }
}