package io.github.acedroidx.shark7.ui.compose

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import io.github.acedroidx.shark7.AlarmService
import io.github.acedroidx.shark7.model.MyAudioAttributes

object ServiceTest {
    @Composable
    fun Composable() {
        val baseContext = LocalContext.current
        var audioAttr by remember { mutableStateOf(MyAudioAttributes.USAGE_ASSISTANT) }
        Column {
            Row {
                Button(onClick = {
                    startService(baseContext, audioAttr)
                }) {
                    Text(text = "StartService")
                }
                Button(onClick = { stopService(baseContext) }) {
                    Text(text = "StopService")
                }
            }
            audioAttrCompose(audioAttr) { audioAttr = it }
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
}