package org.example.frikollection_mobile_desktop.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.example.frikollection_mobile_desktop.api.AppConfig.base_url
import org.example.frikollection_mobile_desktop.models.collection.AddProductToCollectionDto
import org.example.frikollection_mobile_desktop.models.collection.CollectionDto
import org.example.frikollection_mobile_desktop.models.collection.CollectionPreviewDto
import org.example.frikollection_mobile_desktop.models.collection.CollectionStatsDto
import org.example.frikollection_mobile_desktop.models.collection.CreateCollectionDto
import org.example.frikollection_mobile_desktop.models.collection.UpdateCollectionDto
import org.example.frikollection_mobile_desktop.models.collection.FollowedCollectionDto
import org.example.frikollection_mobile_desktop.models.collection.UserCollectionDto
import org.example.frikollection_mobile_desktop.models.product.ProductDto
import org.example.frikollection_mobile_desktop.models.product.ProductTypeDto
import org.example.frikollection_mobile_desktop.models.collection.UserFollowedCollectionDto
import org.example.frikollection_mobile_desktop.models.user.NotificationDto
import org.example.frikollection_mobile_desktop.models.user.UpdateUserDto
import org.example.frikollection_mobile_desktop.models.user.UserDto
import org.example.frikollection_mobile_desktop.models.user.UserProfileDto
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

        install(HttpTimeout) {
            requestTimeoutMillis = 30000     // Temps màxim total d'espera per la resposta
            connectTimeoutMillis = 10000     // Temps màxim per establir connexió
            socketTimeoutMillis = 60000      // Temps màxim d'inactivitat mentre es llegeix o escriu
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
            AppConfig.loggedUsername = loginResponse.username
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
    suspend fun getUserCollections(userId: String): List<UserCollectionDto> {
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

        return response.body()
    }

    // CARREGAR ESTADISTIQUES DE COL·LECCIONS
    suspend fun getCollectionStats(collectionId: String): CollectionStatsDto {
        val response: HttpResponse = client.get("$baseUrl/api/collections/$collectionId/stats") {
            contentType(ContentType.Application.Json)
        }

        return response.body()
    }

    // CREAR COL·LECCIO
    suspend fun createCollection(userId: String, name: String, isPrivate: Boolean) {
        val dto = CreateCollectionDto(userId = userId, name = name, private = isPrivate)
        client.post("${baseUrl}/api/collections") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }
    }

    // MODIFICAR COL·LECCIO
    suspend fun updateCollection(collectionId: String, name: String, private: Boolean) {
        val response = client.put("${base_url}/api/collections/$collectionId") {
            contentType(ContentType.Application.Json)
            setBody(UpdateCollectionDto(name = name, private = private))
        }

        if (!response.status.isSuccess()) {
            val errorBody = response.bodyAsText()
            throw Exception("Error actualitzant col·lecció: ${response.status}. $errorBody")
        }
    }

    // ELIMINAR COL·LECCIO
    suspend fun deleteCollection(collectionId: String) {
        val response = client.delete("${base_url}/api/collections/$collectionId")

        if (!response.status.isSuccess()) {
            val errorBody = response.bodyAsText()
            throw Exception("Error eliminant col·lecció: ${response.status}. $errorBody")
        }
    }
    // AFEGIR PRODUCTE A COL·LECCIO
    suspend fun addProductsToCollection(collectionId: String, productIds: List<String>): AddProductsResponse {
        val response = client.post("$base_url/api/collections/$collectionId/products") {
            contentType(ContentType.Application.Json)
            setBody(AddProductToCollectionDto(collectionId, productIds))
        }
        return response.body()
    }

    // ELIMINAR PRODUCTE DE COL·LECCIO
    suspend fun removeProductFromCollection(collectionId: String, productId: String): Boolean {
        val response = client.delete("$base_url/api/collections/$collectionId/products/$productId")
        return response.status == HttpStatusCode.OK
    }

    // CARREGAR DADES USUARI
    suspend fun getUserProfile(userId: String): UserProfileDto {
        val response = client.get("${AppConfig.base_url}/api/users/$userId/private-profile")
        if (!response.status.isSuccess()) {
            throw Exception("Error loading profile")
        }
        return response.body()
    }

    // ACTUALITZAR DADES USUARI
    suspend fun updateUser(userId: String, dto: UpdateUserDto): UserProfileDto {
        val response = client.put("$baseUrl/api/users/$userId") {
            contentType(ContentType.Application.Json)
            setBody(dto)
        }

        if (!response.status.isSuccess()) {
            val body = response.bodyAsText()
            val message = try {
                Json.parseToJsonElement(body).jsonObject["error"]?.jsonPrimitive?.content
                    ?: "Error desconegut."
            } catch (e: Exception) {
                "Error inesperat: ${response.status}"
            }
            throw Exception(message)
        }

        return response.body()
    }

    // ACTUALITZAR AVATAR USUARI
    suspend fun uploadAvatar(userId: String, fileBytes: ByteArray, fileName: String): String {
        val response: HttpResponse = client.submitFormWithBinaryData(
            url = "$baseUrl/api/uploads/avatar/$userId",
            formData = formData {
                append("File", fileBytes, Headers.build {
                    append(HttpHeaders.ContentType, "image/png")
                    append(HttpHeaders.ContentDisposition, "filename=$fileName")
                })
            }
        )

        if (!response.status.isSuccess()) {
            throw Exception("Error uploading avatar: ${response.status}")
        }

        val responseBody = response.bodyAsText()
        val json = Json.parseToJsonElement(responseBody).jsonObject
        return json["url"]?.jsonPrimitive?.content
            ?: throw Exception("Missing avatar URL in server response")
    }

    // CARREGAR NOTIFICACIONS
    suspend fun getAllNotifications(userId: String): List<NotificationDto> {
        return client.get("$baseUrl/api/users/$userId/notifications").body()
    }

    // CARREGAR QNT NOTIFICACIONS NO LLEGIDES
    suspend fun getUnreadNotificationCount(userId: String): Int {
        return client.get("$baseUrl/api/users/$userId/notifications/unread-count").body()
    }

    // ELIMINAR NOTIFICACIO SELECCIONADA
    suspend fun deleteNotification(userId: String, notificationId: String) {
        client.delete("$baseUrl/api/users/$userId/notifications/$notificationId")
    }

    // ELIMINAR TOTES LES NOTIFICACIONS
    suspend fun deleteAllNotifications(userId: String) {
        client.delete("$baseUrl/api/users/$userId/notifications")
    }

    // CARREGAR ALL USERS
    suspend fun getAllUsers(): List<UserDto> {
        val response = client.get("$base_url/api/users")
        return response.body()
    }

    // CARREGAR ALL COLECTIONS
    suspend fun getAllCollections(): List<CollectionPreviewDto> {
        val response = client.get("$base_url/api/collections")
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

@Serializable
data class AddProductsResponse(
    val added: List<String> = emptyList(),
    val alreadyExists: List<String> = emptyList()
)