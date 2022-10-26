package io.github.acedroidx.shark7

import android.app.Notification
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.IBinder
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import java.io.IOException


class AlarmService : Service() {
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private lateinit var vibrator: Vibrator

    override fun onCreate() {
        super.onCreate()
        mediaPlayer.isLooping = true
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val audioAttrUsage = intent?.getIntExtra(
            "AudioAttributes", MyAudioAttributes.USAGE_ASSISTANT.value
        ) ?: MyAudioAttributes.USAGE_ASSISTANT.value
        val disableAlarmIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            putExtra(EXTRA_NOTIFICATION_ID, 0)
            putExtra("disable_alarm", true)
        }
        val disableAlarmPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, disableAlarmIntent, PendingIntent.FLAG_MUTABLE)
        val stopOnceAlarmIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            putExtra(EXTRA_NOTIFICATION_ID, 0)
        }
        val stopOnceAlarmPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, stopOnceAlarmIntent, PendingIntent.FLAG_MUTABLE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "AlarmService"
//            val descriptionText = ""
            val importance = NotificationManager.IMPORTANCE_MAX
            val mChannel = NotificationChannel("AlarmService", name, importance)
            mChannel.setSound(null, null)
            mChannel.enableVibration(false)
//            mChannel.description = descriptionText
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
        val notification: Notification = NotificationCompat.Builder(this, "AlarmService")
            .setContentTitle("Ring")
            .setContentText("test")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setSilent(true)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setPriority(NotificationCompat.PRIORITY_MAX)
//            .setFullScreenIntent(pendingIntent, true)
            .addAction(R.drawable.ic_launcher_foreground, "全局关闭", disableAlarmPendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "停止本次", stopOnceAlarmPendingIntent)
            .build()

        val ringtone = RingtoneManager.getActualDefaultRingtoneUri(
            this.baseContext,
            RingtoneManager.TYPE_ALARM
        )
        if (isHeadphone() && !mediaPlayer.isPlaying) {
            try {
                mediaPlayer.setDataSource(this.baseContext, ringtone)
                val audioAttr =
                    AudioAttributes.Builder().setUsage(audioAttrUsage)
                        .build()
                mediaPlayer.setAudioAttributes(audioAttr)
                mediaPlayer.setOnPreparedListener { mediaPlayer -> mediaPlayer.start() }
                mediaPlayer.prepareAsync()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
        val pattern = longArrayOf(0, 100, 1000)
        val vibAttr =
            VibrationAttributes.Builder().setUsage(VibrationAttributes.USAGE_ALARM).build()
        vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0), vibAttr)

        startForeground(1, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        vibrator.cancel();
    }

    fun isHeadphone(): Boolean {
        val audioManager = baseContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        audioDevices.forEach { if (AudioDeviceType.isInList(it.type)) return true }
        return false
    }
}