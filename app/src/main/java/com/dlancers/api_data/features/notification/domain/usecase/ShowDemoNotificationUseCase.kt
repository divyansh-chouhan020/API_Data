package com.dlancers.api_data.features.notification.domain.usecase

import com.dlancers.api_data.features.notification.domain.model.NotificationResult
import com.dlancers.api_data.features.notification.domain.repository.NotificationRepository

class ShowDemoNotificationUseCase constructor(
    private val notificationRepository: NotificationRepository,
) {

    operator fun invoke(): NotificationResult {
        return notificationRepository.showDemoNotification()
    }
}