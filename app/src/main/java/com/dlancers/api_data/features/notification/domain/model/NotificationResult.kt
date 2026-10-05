package com.dlancers.api_data.features.notification.domain.model

sealed class NotificationResult {
    // What should be the notification result
    // I think here it should be states like Shown , Disabled , Failed these are the results
    data object Shown : NotificationResult()
    data object Disabled : NotificationResult()
    data class Failure (val message: String): NotificationResult()

}