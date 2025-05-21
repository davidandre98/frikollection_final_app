package org.example.frikollection_mobile_desktop.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.example.frikollection_mobile_desktop.models.collection.CollectionDto
import org.example.frikollection_mobile_desktop.models.collection.CollectionStatsDto
import org.example.frikollection_mobile_desktop.models.collection.FollowedCollectionDto
import org.example.frikollection_mobile_desktop.models.collection.UserCollectionDto
import org.example.frikollection_mobile_desktop.models.product.ProductDto
import org.example.frikollection_mobile_desktop.models.product.ProductTypeDto
import org.example.frikollection_mobile_desktop.models.user.UserDto
import org.example.frikollection_mobile_desktop.register.RegisterUserRequest

object ApiClient {
    private const val baseUrl = AppConfig.base_url

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
            })
        }

        install(Logging) {
            level = LogLevel.BODY
        }

        HttpResponseValidator {
            validateResponse { response ->
                if (!response.status.isSuccess()) {
                    val errorBody = response.bodyAsText()
                    throw Exception("Error ${response.status}: $errorBody")
                }
            }
        }
    }

    // LOGIN USUARI
    suspend fun login(identifier: String, password: String): LoginResponse {
        val response: HttpResponse = client.post("$baseUrl/api/users/login") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(identifier, password))
        }

        if (response.status.isSuccess()) {
            val loginResponse = response.body<LoginResponse>()
            AppConfig.loggedUserId = loginResponse.userId
            return loginResponse
        } else {
            val body = response.bodyAsText()
            var errorMsg = Json.decodeFromString<Map<String, String>>(body)["message"]
            errorMsg = if (errorMsg!!.length > 20) "Login error" else errorMsg

            throw Exception(errorMsg)
        }
    }

    // REGISTRE USUARI
    suspend fun registerUser(data: RegisterUserRequest): String {
        val response = client.post("$baseUrl/api/users") {
            contentType(ContentType.Application.Json)
            setBody(data)
        }

        return if (response.status.isSuccess()) {
            val json = response.bodyAsText()
            Json.decodeFromString<Map<String, String>>(json)["message"] ?: "User created"
        } else {
            val error = response.bodyAsText()
            Json.decodeFromString<Map<String, String>>(error)["error"] ?: "Registration failed"
        }
    }

    // CARREGAR IMATGES CARROUSSEL HOME
    suspend fun getCarouselImages(): List<String> {
        val response: HttpResponse = client.get("$baseUrl/api/uploads/images/carroussel") {
            contentType(ContentType.Application.Json)
        }
        val relativePaths = response.body<List<String>>()
        return relativePaths.map { "$baseUrl$it" }
    }

    // CARREGAR TOTS ELS PRODUCTES
    suspend fun getAllProducts(): List<ProductDto> {
        val response: HttpResponse = client.get("$baseUrl/api/products") {
            contentType(ContentType.Application.Json)
        }

        return response.body()
    }

    // CARREGAR TIPUS DE PRODUCTES
    suspend fun getProductTypes(): List<String> {
        val response: HttpResponse = client.get("$baseUrl/api/producttypes") {
            contentType(ContentType.Application.Json)
        }

        val result = response.body<List<ProductTypeDto>>()
        return result.mapNotNull { it.typeName }
    }

    // CARREGAR COL·LECCIONS DE L'USUARI
    suspend fun getUserCollections(userId: String): List<CollectionDto> {
        val response = client.get("${baseUrl}/api/users/$userId/collections") {
            contentType(ContentType.Application.Json)
        }

        return response.body()
    }

    // CARREGAR COL·LECCIONS SEGUIDES PER L'USUARI
    suspend fun getFollowedCollections(userId: String): List<FollowedCollectionDto> {
        val response = client.get("${baseUrl}/api/users/$userId/collections/followed") {
            contentType(ContentType.Application.Json)
        }

        val userDto = response.body<UserDto>()
        return userDto.followedCollections
    }

    // CARREGAR ESTADISTIQUES DE COL·LECCIONS
    suspend fun getCollectionStats(collectionId: String): CollectionStatsDto {
        val response: HttpResponse = client.get("$baseUrl/api/collections/$collectionId/stats") {
            contentType(ContentType.Application.Json)
        }

        return response.body()
    }
}

@Serializable
data class LoginRequest(
    val identifier: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val userId: String,
    val username: String,
    val nickname: String,
    val avatar: String,
    val message: String
)