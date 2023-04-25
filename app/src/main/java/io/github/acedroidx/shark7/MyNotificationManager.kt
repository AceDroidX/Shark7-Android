package io.github.acedroidx.shark7

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyNotificationManager @Inject constructor(@ApplicationContext val context: Context) {
    var notificationId = 100
    fun createAlarmChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "AlarmService"
//            val descriptionText = ""
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel("AlarmService", name, importance)
            mChannel.setSound(null, null)
            mChannel.enableVibration(false)
//            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager =
                context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    fun createEventChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "EventChannel"
//            val descriptionText = ""
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel("EventChannel", name, importance)
//            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager =
                context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    fun sendEvent(event: Shark7Event) {
        createEventChannel()
        val notification: Notification =
            NotificationCompat.Builder(context, "EventChannel")
                .setContentTitle(event.getTitle())
                .setContentText(event.msg)
                .setStyle(NotificationCompat.BigTextStyle().bigText(event.msg))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setGroup("io.github.acedroidx.shark7.EVENT_GROUP")
                .build()
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("sendEvent", "Manifest.permission.POST_NOTIFICATIONS not granted")
            return
        }
        NotificationManagerCompat.from(context).notify(notificationId, notification)
        notificationId++
    }
}