package io.github.acedroidx.shark7

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.acedroidx.shark7.model.MyAudioAttributes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(@ApplicationContext val context: Context) {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    fun getEnableAlarm() = context.dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey("enable_alarm")] ?: false
    }

    suspend fun setEnableAlarm(value: Boolean) = context.dataStore.edit { preferences ->
        preferences[booleanPreferencesKey("enable_alarm")] = value
    }

    fun getAlarmScope() = context.dataStore.data.map { preferences ->
        preferences[stringSetPreferencesKey("alarm_scope")] ?: emptySet()
    }

    suspend fun setAlarmScope(value: Set<String>) = context.dataStore.edit { preferences ->
        preferences[stringSetPreferencesKey("alarm_scope")] = value
    }

    fun getAudioAttributes(): Flow<MyAudioAttributes> = context.dataStore.data.map { preferences ->
        preferences[stringPreferencesKey("audio_attributes")]?.let { MyAudioAttributes.valueOf(it) }
            ?: MyAudioAttributes.USAGE_ASSISTANT
    }

    suspend fun setAudioAttributes(value: MyAudioAttributes) =
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("audio_attributes")] = value.name
        }

    fun getHeadphoneOnly(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey("headphone_only")] ?: true
    }

    suspend fun setHeadphoneOnly(value: Boolean) = context.dataStore.edit { preferences ->
        preferences[booleanPreferencesKey("headphone_only")] = value
    }

    fun getEnableAudio(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey("enable_audio")] ?: true
    }

    suspend fun setEnableAudio(value: Boolean) = context.dataStore.edit { preferences ->
        preferences[booleanPreferencesKey("enable_audio")] = value
    }

    fun getEnableGadgetCall(): Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey("enable_gadget_call")] ?: false
    }

    suspend fun setEnableGadgetCall(value: Boolean) = context.dataStore.edit { preferences ->
        preferences[booleanPreferencesKey("enable_gadget_call")] = value
    }

    fun getPauseAlarmTo(): Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[longPreferencesKey("pause_alarm_to")] ?: 0
    }

    suspend fun setPauseAlarmTo(value: Long) = context.dataStore.edit { preferences ->
        preferences[longPreferencesKey("pause_alarm_to")] = value
    }
}