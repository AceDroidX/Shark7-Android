package io.github.acedroidx.shark7

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.media.MediaRouter
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    val token = MutableLiveData<String>().apply { value = "" }
    val routeInfo = MutableLiveData<String>().apply { value = "" }
    val audioDevicesInfo = MutableLiveData<String>().apply { value = "" }


    fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("MainViewModel", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val result = task.result

            Log.d("MainViewModel", "Token:$result")
            token.value = result
        })
    }

    fun subscribeTopic(topic: String) {
        Firebase.messaging.subscribeToTopic(topic)
            .addOnCompleteListener { task ->
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