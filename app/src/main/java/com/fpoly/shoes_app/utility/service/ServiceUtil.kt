package com.fpoly.shoes_app.utility.service

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.fpoly.shoes_app.R
import com.fpoly.shoes_app.framework.presentation.MainActivity
import com.fpoly.shoes_app.framework.presentation.channelName
import com.fpoly.shoes_app.utility.SharedPreferencesManager
import com.fpoly.shoes_app.utility.SharedPreferencesManager.getSoundModeState
import com.fpoly.shoes_app.utility.SharedPreferencesManager.getVibrateModeState
import javax.inject.Singleton

@Singleton
object ServiceUtil {
    private const val CHANNEL_ID = "example_channel"
    private const val REQUEST_CODE_NOTIFICATION_PERMISSION = 1001
    @RequiresApi(Build.VERSION_CODES.S)
    fun triggerVibration(context: Context) {
        if (getVibrateModeState()) {
            val vibratorManager =
                context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            val vibrator = vibratorManager.defaultVibrator
            if (vibrator.hasVibrator()) {
                val vibrationEffect =
                    VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator.vibrate(vibrationEffect)
            } else {
                Log.e("Vibration", "Device does not have a vibrator")
            }
        }
    }
    @SuppressLint("MissingPermission")
    fun playNotificationSound(context: Context, title: String, content: String) {
        if (SharedPreferencesManager.getNotificationModeState()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // SDK 33
                if (ContextCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    if (context is Activity) {
                        ActivityCompat.requestPermissions(
                            context,
                            arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                            REQUEST_CODE_NOTIFICATION_PERMISSION
                        )
                    }
                    return
                }
            }
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_IMMUTABLE
            )
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationManager.createNotificationChannel(channel)
            }
            notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
        }
    }
    fun playCustomSound(context: Context) {
        if (getSoundModeState()) {
            val mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.levelup)
            mediaPlayer.start()
        }
    }
}