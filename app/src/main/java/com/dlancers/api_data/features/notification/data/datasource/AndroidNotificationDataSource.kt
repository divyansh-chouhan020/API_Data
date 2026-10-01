package com.dlancers.api_data.features.notification.data.datasource

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dlancers.api_data.features.notification.domain.model.NotificationResult
import com.dlancers.api_data.core.common.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AndroidNotificationDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
) : NotificationDataSource {

    override fun showDemoNotification(): NotificationResult {
        val notificationManagerCompat = NotificationManagerCompat.from(context)

        if (!notificationManagerCompat.areNotificationsEnabled()) {
            return NotificationResult.Disabled
        }

        return try {
            ensureNotificationChannelExists()
            postDemoNotification(notificationManagerCompat)
            NotificationResult.Shown
        } catch (exception: SecurityException) {
            NotificationResult.Failure(
                exception.message ?: NOTIFICATION_PERMISSION_MISSING_MESSAGE,
            )
        } catch (exception: Exception) {
            NotificationResult.Failure(
                exception.message ?: NOTIFICATION_POST_FAILED_MESSAGE,
            )
        }
    }

    private fun ensureNotificationChannelExists() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = Constants.NOTIFICATION_CHANNEL_DESCRIPTION
        }

        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }

    private fun postDemoNotification(notificationManagerCompat: NotificationManagerCompat) {
        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)

            .setSmallIcon(android.R.drawable.ic_dialog_info)
             .setContentTitle(Constants.DEMO_NOTIFICATION_TITLE)
            .setContentText(Constants.DEMO_NOTIFICATION_BODY)
              .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManagerCompat.notify(Constants.DEMO_NOTIFICATION_ID, notification)
    }

    private companion object {
        const val NOTIFICATION_PERMISSION_MISSING_MESSAGE =
            "Notification permission is missing. Please enable notifications and try again."
        const val NOTIFICATION_POST_FAILED_MESSAGE = "Failed to show the notification"
    }
}