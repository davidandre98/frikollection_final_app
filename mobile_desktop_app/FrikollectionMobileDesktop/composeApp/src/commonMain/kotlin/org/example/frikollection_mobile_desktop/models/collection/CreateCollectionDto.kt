package org.example.frikollection_mobile_desktop.models.collection

import kotlinx.serialization.Serializable

@Serializable
data class CreateCollectionDto(
    val userId: String,
    val name: String,
    val private: Boolean
)