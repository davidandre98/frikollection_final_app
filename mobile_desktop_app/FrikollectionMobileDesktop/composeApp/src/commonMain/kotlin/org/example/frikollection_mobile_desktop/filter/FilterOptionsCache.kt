package org.example.frikollection_mobile_desktop.filter

import org.example.frikollection_mobile_desktop.models.product.ProductDto

object FilterOptionsCache {
    var productTypes: List<String> = emptyList()
    var subtypes: List<String> = emptyList()
    var supertypes: List<String> = emptyList()
    var status: List<String> = emptyList()
    var licenses: List<String> = emptyList()
    var tags: List<String> = emptyList()

    fun loadFromProducts(allProducts: List<ProductDto>) {
        productTypes = allProducts.mapNotNull { it.productType?.typeName }.distinct().sorted()
        subtypes = allProducts.mapNotNull { it.subtype }.distinct().sorted()
        supertypes = allProducts.mapNotNull { it.supertype }.distinct().sorted()
        status = allProducts.mapNotNull { it.status }.distinct().sorted()
        licenses = allProducts.mapNotNull { it.license }.distinct().sorted()
        tags = allProducts.flatMap { it.tags.mapNotNull { tag -> tag.name } }.distinct().sorted()
    }
}