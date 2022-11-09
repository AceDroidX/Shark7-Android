package io.github.acedroidx.shark7.ui.compose

import android.app.Service
import android.content.Context
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.media.MediaRouter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import io.github.acedroidx.shark7.AudioDeviceType

class AudioInfo(val routeInfo: MutableState<String>, val audioDevicesInfo: MutableState<String>) {
    companion object {
        @Composable
        fun Composable() {
            val context = LocalContext.current
            val state = rememberState()
            Column {
                Infos(state.routeInfo.value, state.audioDevicesInfo.value)
                Button(onClick = {
                    state.getAudioDeviceInfo(context)
                    state.getRouteInfo(context)
                }) {
                    Text(text = "Refresh Audio Devices")
                }
            }
        }

        @Composable
        fun Infos(routeInfo: String?, audioDeviceInfo: String?) {
            SelectionContainer {
                Column {
                    Text(
                        "RouteInfo: ${routeInfo}\n",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        "AudioDeviceInfo: ${audioDeviceInfo}",
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

        @Composable
        fun rememberState(
            routeInfo: MutableState<String> = rememberSaveable { mutableStateOf("") },
            audioDevicesInfo: MutableState<String> = rememberSaveable { mutableStateOf("") }
        ) = remember { AudioInfo(routeInfo, audioDevicesInfo) }
    }

    fun getRouteInfo(context: Context) {
        val mr = context.getSystemService(Service.MEDIA_ROUTER_SERVICE) as MediaRouter
//        val ri: MediaRouter.RouteInfo = mr.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_AUDIO)
        val ri: MediaRouter.RouteInfo = mr.defaultRoute
        routeInfo.value = ri.toString()
    }

    fun getAudioDeviceInfo(context: Context) {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val audioDevices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        audioDevicesInfo.value =
            audioDevices.map { audioDeviceToString(it) }
                .joinToString("\n")
    }

    fun audioDeviceToString(adi: AudioDeviceInfo): String {
        return "{id=${adi.id}, productName=${adi.productName}, type=${
            AudioDeviceType.findByValue(
                adi.type
            ) ?: adi.type
        }, address=${adi.address}" +
                ", sampleRates=${adi.sampleRates.contentToString()}, audioDescriptors=${adi.audioDescriptors}" +
                ", audioProfiles=${adi.audioProfiles}, isSink=${adi.isSink}, isSource=${adi.isSource}}"
    }
}