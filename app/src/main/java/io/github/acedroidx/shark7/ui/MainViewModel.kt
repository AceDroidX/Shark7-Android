package io.github.acedroidx.shark7.ui

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.acedroidx.shark7.model.MyAudioAttributes
import io.github.acedroidx.shark7.SettingsRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(val settingsRepository: SettingsRepository) : ViewModel() {
    val enableAlarm = settingsRepository.getEnableAlarm()
    val enableVibrate = settingsRepository.getEnableVibrate()
    val enableAudio = settingsRepository.getEnableAudio()
    val audioAttributes = settingsRepository.getAudioAttributes()
    val headphoneOnly = settingsRepository.getHeadphoneOnly()
    val enableGadgetCall = settingsRepository.getEnableGadgetCall()
    val pauseAlarmTo = settingsRepository.getPauseAlarmTo()

    fun setEnableAlarm(value: Boolean) {
        Log.d("MainViewModel", "setEnableAlarm:$value")
        viewModelScope.launch {
            settingsRepository.setEnableAlarm(value)
        }
    }

    fun setAudioAttributes(value: MyAudioAttributes) {
        Log.d("MainViewModel", "setAudioAttributes:$value")
        viewModelScope.launch {
            settingsRepository.setAudioAttributes(value)
        }
    }

    fun setHeadphoneOnly(value: Boolean) {
        Log.d("MainViewModel", "setHeadphoneOnly:$value")
        viewModelScope.launch {
            settingsRepository.setHeadphoneOnly(value)
        }
    }

    fun setEnableVibrate(value: Boolean) {
        Log.d("MainViewModel", "setEnableVibrate:$value")
        viewModelScope.launch {
            settingsRepository.setEnableVibrate(value)
        }
    }

    fun setEnableAudio(value: Boolean) {
        Log.d("MainViewModel", "setEnableAudio:$value")
        viewModelScope.launch {
            settingsRepository.setEnableAudio(value)
        }
    }

    fun setEnableGadgetCall(value: Boolean) {
        Log.d("MainViewModel", "setEnableGadgetCall:$value")
        viewModelScope.launch {
            settingsRepository.setEnableGadgetCall(value)
        }
    }

    fun setPauseAlarmTo(value: Long) {
        Log.d("MainViewModel", "setPauseAlarmTo:$value")
        viewModelScope.launch {
            settingsRepository.setPauseAlarmTo(value)
        }
    }

    fun openDebugActivity(context: Context) {
        val intent = Intent(context, DebugActivity::class.java)
        context.startActivity(intent)
    }

    fun openAlarmScopeActivity(context: Context) {
        val intent = Intent(context, AlarmScopeActivity::class.java)
        context.startActivity(intent)
    }
}