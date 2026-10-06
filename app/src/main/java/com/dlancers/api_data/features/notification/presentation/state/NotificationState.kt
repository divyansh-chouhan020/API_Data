package com.dlancers.api_data.features.notification.presentation.state

sealed class NotificationState {

    data object Idle : NotificationState()

    data object Checking : NotificationState()

    data object Shown : NotificationState()

    data class PermissionDenied(
        val isPermanentlyDenied: Boolean,
    ) : NotificationState()

    data object Disabled : NotificationState()

    data class Error(
        val message: String,
    ) : NotificationState()
}