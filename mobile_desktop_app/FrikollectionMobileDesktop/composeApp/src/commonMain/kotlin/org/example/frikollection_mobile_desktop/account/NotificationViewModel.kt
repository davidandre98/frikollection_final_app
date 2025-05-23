package org.example.frikollection_mobile_desktop.account

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.frikollection_mobile_desktop.api.ApiClient
import org.example.frikollection_mobile_desktop.api.AppConfig
import org.example.frikollection_mobile_desktop.models.user.NotificationDto

class NotificationViewModel : ViewModel() {
    private val scope = CoroutineScope(Dispatchers.Default)

    private val _notifications = MutableStateFlow<List<NotificationDto>>(emptyList())
    val notifications: StateFlow<List<NotificationDto>> = _notifications.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadNotifications() {
        val userId = AppConfig.loggedUserId ?: return

        scope.launch {
            try {
                val all = ApiClient.getAllNotifications(userId)
                _notifications.value = all
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun loadUnreadCount() {
        val userId = AppConfig.loggedUserId ?: return

        scope.launch {
            try {
                val count = ApiClient.getUnreadNotificationCount(userId)
                _unreadCount.value = count
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteNotification(notificationId: String) {
        val userId = AppConfig.loggedUserId ?: return

        scope.launch {
            try {
                ApiClient.deleteNotification(userId, notificationId)
                loadNotifications()
                loadUnreadCount()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteAllNotifications() {
        val userId = AppConfig.loggedUserId ?: return

        scope.launch {
            try {
                ApiClient.deleteAllNotifications(userId)
                loadNotifications()
                loadUnreadCount()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}