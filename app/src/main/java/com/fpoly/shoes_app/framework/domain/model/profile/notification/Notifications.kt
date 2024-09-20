package com.fpoly.shoes_app.framework.domain.model.profile.notification

data class NotificationsHomeResponse(
    val notifications: List<NotificationsHome>
)

data class NotificationsHome(
    val __v: Int?,
    val _id: String?,
    val body: String?,
    val image: String?,
    val time: String?,
    val title: String?,
    val typeNotification: String?,
    val userId: String?
)
