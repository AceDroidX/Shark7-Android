package io.github.acedroidx.shark7

import android.app.Notification
import android.app.Notification.EXTRA_NOTIFICATION_ID
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
import android.os.PowerManager
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject
import io.github.acedroidx.shark7.GadgetCall.sendGadgetCall
import io.github.acedroidx.shark7.model.AlarmConfig
import io.github.acedroidx.shark7.model.AudioDeviceType
import io.github.acedroidx.shark7.model.MyAudioAttributes
import io.github.acedroidx.shark7.model.Shark7Event

@AndroidEntryPoint
class AlarmService : Service() {
    @Inject
    lateinit var myNotificationManager: MyNotificationManager

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private lateinit var vibrator: Vibrator
    private var wakeLock: PowerManager.WakeLock? = null

    override fun onCreate() {
        super.onCreate()
        mediaPlayer.isLooping = true
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION") getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val event = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("Shark7Event", Shark7Event::class.java)
        } else {
            @Suppress("DEPRECATION") intent?.getParcelableExtra("Shark7Event")
        }
        val alarmConfig = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra("AlarmConfig", AlarmConfig::class.java)
        } else {
            @Suppress("DEPRECATION") intent?.getParcelableExtra("AlarmConfig")
        } ?: AlarmConfig(true, MyAudioAttributes.USAGE_ASSISTANT, true, false)

        val disableAlarmIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            putExtra(EXTRA_NOTIFICATION_ID, 0)
            putExtra("disable_alarm", true)
        }
        val disableAlarmPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, disableAlarmIntent, PendingIntent.FLAG_MUTABLE)
        val stopOnceAlarmIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            putExtra(EXTRA_NOTIFICATION_ID, 1)
        }
        val stopOnceAlarmPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 1, stopOnceAlarmIntent, PendingIntent.FLAG_MUTABLE)
        val pause5minAlarmIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            putExtra(EXTRA_NOTIFICATION_ID, 2)
            putExtra("pause_alarm", 5 * 60 * 1000)
        }
        val pause5minAlarmPendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 2, pause5minAlarmIntent, PendingIntent.FLAG_MUTABLE)
        myNotificationManager.createAlarmChannel()
        val content = event?.toString() ?: "null"
        val notification: Notification =
            NotificationCompat.Builder(this, "AlarmService").setContentTitle("Ring")
                .setContentText(content).setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true).setSilent(true).setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX)
//            .setFullScreenIntent(pendingIntent, true)
                .addAction(R.drawable.ic_launcher_foreground, "全局关闭", disableAlarmPendingIntent)
                .addAction(
                    R.drawable.ic_launcher_foreground, "停止本次", stopOnceAlarmPendingIntent
                ).addAction(
                    R.drawable.ic_launcher_foreground, "暂停5分钟", pause5minAlarmPendingIntent
                ).build()

        if (alarmConfig.enableAudio) {
            val ringtone = RingtoneManager.getActualDefaultRingtoneUri(
                this.baseContext, RingtoneManager.TYPE_ALARM
            )
            if ((!alarmConfig.headphoneOnly || isHeadphone()) && !mediaPlayer.isPlaying) {
                try {
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(this.baseContext, ringtone)
                    val audioAttr =
                        AudioAttributes.Builder().setUsage(alarmConfig.audioAttr.value).build()
                    mediaPlayer.setAudioAttributes(audioAttr)
                    mediaPlayer.setOnPreparedListener { mediaPlayer -> mediaPlayer.start() }
                    mediaPlayer.prepareAsync()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
        }
        if (alarmConfig.enableGadgetCall) {
            sendGadgetCall(this, event?.msg ?: "null event")
        }
        val pattern = longArrayOf(0, 100, 1000)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val vibAttr =
                VibrationAttributes.Builder().setUsage(VibrationAttributes.USAGE_ALARM).build()
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0), vibAttr)
        } else {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
        }

        val timerTask = object : TimerTask() {
            override fun run() {
                stopSelf()
            }
        }
        val timer = Timer()
        timer.schedule(timerTask, 3 * 60 * 1000)

        startForeground(1, notification)

        wakeLock = (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
            newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Shark7::MyWakelockTag").apply {
                acquire(3 * 60 * 1000L)
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying) mediaPlayer.stop()
        vibrator.cancel();
        wakeLock?.release()
    }

    fun isHeadphone(): Boolean {
        val audioManager = baseContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        audioDevices.forEach { if (AudioDeviceType.isInList(it.type)) return true }
        return false
    }
}