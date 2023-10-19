package io.github.acedroidx.shark7

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MyBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var settingsRepository: SettingsRepository
    override fun onReceive(context: Context, intent: Intent) {
        val intentService = Intent(
            context, AlarmService::class.java
        )
        if (intent.getBooleanExtra("disable_alarm", false)) {
            @OptIn(DelicateCoroutinesApi::class) GlobalScope.launch(Dispatchers.IO) {
                settingsRepository.setEnableAlarm(false)
            }
        } else if (intent.hasExtra("pause_alarm")) {
            val pauseTime = intent.getIntExtra("pause_alarm", 0)
            if (pauseTime > 0) {
                @OptIn(DelicateCoroutinesApi::class) GlobalScope.launch(Dispatchers.IO) {
                    settingsRepository.setPauseAlarmTo(System.currentTimeMillis() + pauseTime)
                }
            }
        }
        context.stopService(intentService)
    }
}