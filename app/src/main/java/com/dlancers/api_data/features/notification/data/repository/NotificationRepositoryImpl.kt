package com.dlancers.api_data.features.notification.data.repository

import com.dlancers.api_data.features.notification.data.datasource.NotificationDataSource
import com.dlancers.api_data.features.notification.domain.model.NotificationResult
import com.dlancers.api_data.features.notification.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationDataSource: NotificationDataSource,
) : NotificationRepository {

    override fun showDemoNotification(): NotificationResult {
        return notificationDataSource.showDemoNotification()
    }
}