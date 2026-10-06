package com.dlancers.api_data.features.notification.data.datasource

import com.dlancers.api_data.features.notification.domain.model.NotificationResult

interface NotificationDataSource {

    fun showDemoNotification(): NotificationResult
}

