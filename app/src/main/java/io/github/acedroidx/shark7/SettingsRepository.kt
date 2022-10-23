package io.github.acedroidx.shark7

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
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
        preferences[booleanPreferencesKey(
            "enable_alarm"
        )] = value
    }

}