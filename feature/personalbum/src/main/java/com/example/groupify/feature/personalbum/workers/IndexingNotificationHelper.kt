// feature/personalbum/src/main/.../workers/IndexingNotificationHelper.kt
package com.example.groupify.feature.personalbum.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Creates and updates notifications for the face-indexing foreground service.
 *
 * The notification channel is created eagerly in [init] — channel creation is idempotent
 * so calling it multiple times is harmless.
 */
@Singleton
class IndexingNotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val notificationManager = NotificationManagerCompat.from(context)

    /** Small-icon resource: we use the app's own launcher icon (always present). */
    private val smallIconRes: Int = context.applicationInfo.icon

    init {
        createChannel()
    }

    private fun createChannel() {
        // NotificationChannel and all channel-specific APIs require API 26+.
        // On API 24-25 channels are ignored by the OS; NotificationCompat handles
        // the fallback automatically, so no channel setup is needed there.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW, // silent — no sound/vibration for progress updates
            ).apply {
                description = "Shows progress while Groupify indexes photos for face recognition."
                setShowBadge(false)
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Builds a progress notification shown while the worker is active.
     * Pass [current] == 0 and [total] == 0 for the initial "starting" state.
     */
    fun buildProgressNotification(current: Int, total: Int): Notification {
        val contentText = when {
            total > 0 -> "Processing $current of $total photos"
            else -> "Starting…"
        }
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Indexing your photos")
            .setContentText(contentText)
            .setSmallIcon(smallIconRes)
            .setOngoing(true)        // cannot be dismissed by the user while service is running
            .setOnlyAlertOnce(true)  // don't buzz/ring on every update
            .apply {
                if (total > 0) {
                    setProgress(total, current, /* indeterminate= */ false)
                } else {
                    setProgress(0, 0, /* indeterminate= */ true)
                }
            }
            .build()
    }

    /**
     * Posts a one-shot "completed" notification after the worker finishes.
     * Silently skipped on API 33+ when POST_NOTIFICATIONS permission is not granted.
     */
    fun showCompletionNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Face indexing completed")
            .setContentText("Your photo library is ready to search.")
            .setSmallIcon(smallIconRes)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(COMPLETION_NOTIFICATION_ID, notification)
    }

    companion object {
        const val CHANNEL_ID = "face_indexing"
        private const val CHANNEL_NAME = "Face Indexing"
        const val PROGRESS_NOTIFICATION_ID = 1001
        private const val COMPLETION_NOTIFICATION_ID = 1002
    }
}
