package com.fpoly.shoes_app.framework.repository

import com.fpoly.shoes_app.framework.data.dataremove.api.getInterface.NotificationsInterface
import com.fpoly.shoes_app.framework.domain.model.profile.notification.NotificationsHome
import javax.inject.Inject

class NotificationsRepository @Inject constructor(
    private val notificationsInterface: NotificationsInterface
) {

    suspend fun loadNotifications(userId: String): Result<List<NotificationsHome>> {
        return try {
            val response = notificationsInterface.getNotificationsByUser(userId)

            if (response.isSuccessful) {
                val notifications = response.body()
                if (notifications != null) {
                    Result.success(notifications)
                } else {
                    Result.failure(Exception("Empty response"))
                }
            } else {
                Result.failure(Exception("Failed to load notifications"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
