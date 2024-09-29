package io.github.acedroidx.shark7

import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import io.github.acedroidx.shark7.model.AlarmConfig
import io.github.acedroidx.shark7.model.Shark7Event
import io.github.acedroidx.shark7.model.Shark7FcmData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var myNotificationManager: MyNotificationManager

    // https://stackoverflow.com/questions/63405673/how-to-call-suspend-function-from-service-android
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("MyFirebaseMessagingService", "onNewToken:$token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("MyFirebaseMessagingService", "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("MyFirebaseMessagingService", "Message data payload: ${remoteMessage.data}")
            val data = Shark7FcmData(remoteMessage.data)
            val event = Gson().fromJson(data.event, Shark7Event::class.java)
            if (data.is_show_notification != "false") {
                myNotificationManager.sendEvent(event)
            }
            runBlocking {
                if (settingsRepository.getEnableAlarm().first()) {
                    if (settingsRepository.getPauseAlarmTo().first() > System.currentTimeMillis()) {
                        // 暂停Alarm中
                        Log.d(
                            "MyFirebaseMessagingService", "getPauseAlarmTo() ${
                                settingsRepository.getPauseAlarmTo().first()
                            } > currentTimeMillis() ${System.currentTimeMillis()}"
                        )
                    } else if (settingsRepository.getAlarmScope().first().contains(event.scope)) {
                        val alarmConfig = AlarmConfig(
                            settingsRepository.getEnableVibrate().first(),
                            settingsRepository.getEnableAudio().first(),
                            settingsRepository.getAudioAttributes().first(),
                            settingsRepository.getHeadphoneOnly().first(),
                            settingsRepository.getEnableGadgetCall().first()
                        )
                        val intentService = Intent(baseContext, AlarmService::class.java)
                        intentService.putExtra("Shark7Event", event)
                        intentService.putExtra("AlarmConfig", alarmConfig)
                        baseContext.startService(intentService)
                    }
                }
            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("MyFirebaseMessagingService", "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.


    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}