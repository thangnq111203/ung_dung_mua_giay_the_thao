package com.fpoly.shoes_app.framework.presentation

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.net.URL


const val channelId = "notification_channel"
const val channelName = "com.fpoly.shoes_app"

@AndroidEntryPoint
class MyFirebaseMessagingService:
FirebaseMessagingService() {
    var TAG = "MyFirebaseMesaggingService"

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data.isNotEmpty()) {
            Log.d(TAG, "Message Data payload: " + message.data)
        }
        if (message.notification != null) {
            sendNotification(
                message.notification!!.body, message.notification!!.title, message.notification!!
                    .imageUrl
            )
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    private fun sendNotification(messageBody: String?, title: String?, imgUrl: Uri?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        var bmp: Bitmap? = null
        Log.d(TAG, "sendNotification: " + imgUrl.toString())
        try {
            val `in` = URL(imgUrl.toString()).openStream()
            bmp = BitmapFactory.decodeStream(`in`)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        if (SharedPreferencesManager.getNotificationModeState()){
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setStyle(NotificationCompat.BigPictureStyle().bigPicture(bmp))
                .setPriority(Notification.PRIORITY_HIGH)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }}
}