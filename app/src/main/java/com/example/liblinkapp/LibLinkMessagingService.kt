// app/src/main/java/com/example/liblinkapp/LibLinkMessagingService.kt
package com.example.liblinkapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class LibLinkMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Save the token to SharedPreferences so you can send it to your backend
        getSharedPreferences("LibLinkSettings", Context.MODE_PRIVATE)
            .edit()
            .putString("fcmToken", token)
            .apply()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Check if notifications are enabled in user preferences
        val prefs = getSharedPreferences("LibLinkSettings", Context.MODE_PRIVATE)
        val notificationsEnabled = prefs.getBoolean("notificationsEnabled", true)
        if (!notificationsEnabled) return

        val title = remoteMessage.notification?.title
            ?: remoteMessage.data["title"]
            ?: "LibLink"
        val body = remoteMessage.notification?.body
            ?: remoteMessage.data["body"]
            ?: ""

        sendNotification(title, body)
    }

    private fun sendNotification(title: String, body: String) {
        val channelId = "liblink_channel"
        val intent = Intent(this, HomeActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create channel for Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "LibLink Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Library updates, due dates, and link requests"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}