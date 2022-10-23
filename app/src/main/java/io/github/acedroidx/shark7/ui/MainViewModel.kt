package io.github.acedroidx.shark7.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.acedroidx.shark7.AlarmService
import io.github.acedroidx.shark7.MyAudioAttributes
import io.github.acedroidx.shark7.SettingsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(val settingsRepository: SettingsRepository) : ViewModel() {
    val enableAlarm = settingsRepository.getEnableAlarm()

    fun setEnableAlarm(value: Boolean) {
        Log.d("MainViewModel","setEnableAlarm:$value")
        viewModelScope.launch {
            settingsRepository.setEnableAlarm(value)
        }
    }

    fun subscribeTopic(topic: String) {
        Firebase.messaging.subscribeToTopic(topic).addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscribe failed"
                }
                Log.d("MainViewModel", "subscribeTopic:$msg")
            }
    }

    fun startService(context: Context, audioAttributes: MyAudioAttributes) {
        val intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra("AudioAttributes", audioAttributes.value)
        context.startService(intentService)
    }

    fun stopService(context: Context) {
        val intentService = Intent(context, AlarmService::class.java)
        context.stopService(intentService)
    }

    fun openDebugActivity(context: Context) {
        val intent = Intent(context, DebugActivity::class.java)
        context.startActivity(intent)
    }
}