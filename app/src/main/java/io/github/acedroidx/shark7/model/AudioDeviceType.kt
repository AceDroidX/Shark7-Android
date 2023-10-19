package io.github.acedroidx.shark7.model

import android.media.AudioDeviceInfo

enum class AudioDeviceType(val value: Int) {
    TYPE_BLUETOOTH_A2DP(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP),
    TYPE_BLUETOOTH_SCO(AudioDeviceInfo.TYPE_BLUETOOTH_SCO);

    companion object {
        fun findByValue(value: Int): AudioDeviceType? {
            AudioDeviceType.values().forEach { if (it.value == value) return it }
            return null
        }

        fun isInList(value: Int): Boolean {
            return findByValue(value) != null
        }
    }
}