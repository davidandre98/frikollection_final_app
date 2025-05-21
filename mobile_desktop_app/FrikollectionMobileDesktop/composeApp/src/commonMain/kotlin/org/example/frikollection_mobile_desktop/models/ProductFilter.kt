package org.example.frikollection_mobile_desktop.models

data class ProductFilter(
    val supertype: String? = null,
    val subtype: String? = null,
    val status: String? = null,
    val license: String? = null,
    val productType: String? = null,
    val tags: List<String> = emptyList()
)

data class ProductListState(
    val type: String? = null,
    val status: String? = null,
    val filter: ProductFilter = ProductFilter()
)