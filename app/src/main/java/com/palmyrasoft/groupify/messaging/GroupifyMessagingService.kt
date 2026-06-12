package com.palmyrasoft.groupify.messaging

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.palmyrasoft.groupify.BuildConfig
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Receives Firebase Cloud Messaging pushes.
 *
 * Hilt injects [PushNotificationHelper] (fields are ready after super.onCreate(), which the
 * framework calls before any callback). The service is registered with the MESSAGING_EVENT
 * intent-filter in AndroidManifest.xml.
 *
 * Display behaviour:
 *  - **Notification messages** sent while the app is in the *background* are rendered by the
 *    FCM SDK itself using the manifest's default channel/icon meta-data — onMessageReceived is
 *    NOT called for those.
 *  - **Notification messages received in the foreground**, and **all data-only messages**, are
 *    delivered here and surfaced via [PushNotificationHelper].
 */
@AndroidEntryPoint
class GroupifyMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationHelper: PushNotificationHelper

    override fun onMessageReceived(message: RemoteMessage) {
        // Prefer the notification payload; fall back to data keys for data-only messages.
        val title = message.notification?.title ?: message.data["title"]
        val body = message.notification?.body ?: message.data["body"]

        // Nothing to display (e.g. a silent data sync ping) — ignore.
        if (title == null && body == null) return

        notificationHelper.show(title = title, body = body, messageId = message.messageId)
    }

    override fun onNewToken(token: String) {
        // No app server to register with — log for manual single-device testing from the
        // Firebase console. Broadcast delivery is handled via the "all" topic subscription
        // in GroupifyApp.onCreate().
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "FCM registration token refreshed: $token")
        }
    }

    companion object {
        private const val TAG = "GroupifyMessaging"
    }
}
