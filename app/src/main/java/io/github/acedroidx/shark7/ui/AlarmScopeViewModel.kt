package io.github.acedroidx.shark7.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.acedroidx.shark7.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmScopeViewModel @Inject constructor(val settingsRepository: SettingsRepository) :
    ViewModel() {
    val alarmScope: Flow<Set<String>> = settingsRepository.getAlarmScope()
    fun setAlarmScope(id: String, value: Boolean) {
        viewModelScope.launch {
            val newAlarmScope =
                if (value) alarmScope.first().plus(id) else alarmScope.first().minus(id)
            settingsRepository.setAlarmScope(newAlarmScope)
        }
    }
}