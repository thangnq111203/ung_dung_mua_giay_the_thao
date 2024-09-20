package com.fpoly.shoes_app.framework.presentation.ui.home.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fpoly.shoes_app.framework.domain.model.profile.notification.NotificationsHome
import com.fpoly.shoes_app.framework.repository.NotificationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationHomeViewModel @Inject constructor(
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    private val _notificationsState = MutableStateFlow(NotificationState())
    val notificationsState: StateFlow<NotificationState> = _notificationsState

    private var isLastPage = false

    fun fetchNotifications(userId: String) {
        if (isLastPage || _notificationsState.value.isLoading) return

        viewModelScope.launch {
            _notificationsState.value = NotificationState(isLoading = true)
            try {
                val result = notificationsRepository.loadNotifications(userId)
                if (result.isSuccess) {
                    val notifications = result.getOrNull()
                    if (notifications != null) {
                        isLastPage = notifications.isEmpty()
                        _notificationsState.value = NotificationState(
                            notifications = notifications,
                            isLoading = false
                        )
                    } else {
                        _notificationsState.value = NotificationState(
                            errorMessage = "No notifications found",
                            isLoading = false
                        )
                    }
                } else {
                    _notificationsState.value = NotificationState(
                        errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Unknown error",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _notificationsState.value = NotificationState(
                    errorMessage = e.localizedMessage ?: "Error fetching notifications",
                    isLoading = false
                )
            }
        }
    }

    data class NotificationState(
        val notifications: List<NotificationsHome> = emptyList(),
        val isLoading: Boolean = false,
        val errorMessage: String? = null
    )
}
