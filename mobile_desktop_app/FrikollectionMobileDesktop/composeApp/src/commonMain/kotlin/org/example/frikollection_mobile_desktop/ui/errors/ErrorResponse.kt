package org.example.frikollection_mobile_desktop.ui.errors

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class SimpleMessage(val message: String? = null)

@Serializable
data class ErrorMessage(val error: String? = null)

object ErrorParser {
    fun extract(raw: String?): String {
        if (raw.isNullOrBlank()) return ""

        val jsonStart = raw.indexOf("{")
        val jsonText = if (jsonStart >= 0) raw.substring(jsonStart) else raw

        return try {
            Json.decodeFromString<SimpleMessage>(jsonText).message
                ?: Json.decodeFromString<ErrorMessage>(jsonText).error
                ?: raw
        } catch (e: Exception) {
            raw
        }
    }
}