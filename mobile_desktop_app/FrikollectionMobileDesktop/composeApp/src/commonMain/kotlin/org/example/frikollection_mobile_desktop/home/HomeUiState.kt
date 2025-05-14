package org.example.frikollection_mobile_desktop.home

import org.example.frikollection_mobile_desktop.models.ProductDto

data class HomeUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val allProducts: List<ProductDto> = emptyList(),
    val allFunkoProducts: List<ProductDto> = emptyList(),
    val allTcgProducts: List<ProductDto> = emptyList(),
    val allFigureProducts: List<ProductDto> = emptyList()
)