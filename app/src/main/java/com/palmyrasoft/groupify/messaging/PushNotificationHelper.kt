package com.palmyrasoft.groupify.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.palmyrasoft.groupify.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Creates the channel for and displays Firebase Cloud Messaging push notifications.
 *
 * Mirrors [com.palmyrasoft.groupify.feature.personalbum.workers.IndexingNotificationHelper]:
 * the channel is created eagerly in [init] (idempotent) and display is silently skipped on
 * API 33+ when POST_NOTIFICATIONS has not been granted.
 */
@Singleton
class PushNotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val notificationManager = NotificationManagerCompat.from(context)

    /** Small-icon resource: we use the app's own launcher icon (always present). */
    private val smallIconRes: Int = context.applicationInfo.icon

    init {
        createChannel()
    }

    private fun createChannel() {
        // NotificationChannel APIs require API 26+. On API 24-25 channels are ignored and
        // NotificationCompat handles the fallback automatically.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH, // push alerts should buzz/peek
            ).apply {
                description = "General announcements and updates from Groupify."
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Posts a push notification. Silently skipped on API 33+ when POST_NOTIFICATIONS is not
     * granted — the OS would drop it anyway.
     *
     * @param messageId stable id used as the notification id so repeated sends with the same
     *   id replace rather than stack; falls back to a time-derived id when absent.
     */
    fun show(title: String?, body: String?, messageId: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val contentIntent = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title ?: context.applicationInfo.loadLabel(context.packageManager))
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setSmallIcon(smallIconRes)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // pre-O peek/heads-up
            .setAutoCancel(true)
            .setContentIntent(contentIntent)
            .build()

        val notificationId = messageId?.hashCode() ?: System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notification)
    }

    companion object {
        /** Must match the default_notification_channel_id meta-data in AndroidManifest.xml. */
        const val CHANNEL_ID = "groupify_push"
        private const val CHANNEL_NAME = "Announcements"
    }
}
