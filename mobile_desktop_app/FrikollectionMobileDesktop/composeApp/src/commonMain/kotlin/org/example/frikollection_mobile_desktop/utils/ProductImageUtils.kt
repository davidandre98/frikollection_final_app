package org.example.frikollection_mobile_desktop.utils

import org.example.frikollection_mobile_desktop.api.AppConfig
import org.example.frikollection_mobile_desktop.models.product.ProductDto

fun getCardSectionImage(type: String, products: List<ProductDto>, status: String?): String {
    val filteredProducts = status?.let {
        products.filter { it.status.equals(status, ignoreCase = true) }
    } ?: products

    val firstImage = filteredProducts.firstOrNull()?.smallPicture
    val baseUrl = AppConfig.base_url

    return if (!firstImage.isNullOrBlank()) {
        "$baseUrl$firstImage"
    } else {
        "$baseUrl/images/uploads/logo/logo_${type.lowercase()}.png"
    }
}