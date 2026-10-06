package com.dlancers.api_data.features.notification.domain.repository

import com.dlancers.api_data.features.notification.domain.model.NotificationResult

interface NotificationRepository {

    fun showDemoNotification(): NotificationResult
}