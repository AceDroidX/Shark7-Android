package io.github.acedroidx.shark7.ui.compose

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.json.JSONArray
import org.json.JSONObject


object PebbleTest {
    @Composable
    fun Composable() {
        val baseContext = LocalContext.current
        Button(onClick = { sendPebbleMsg(baseContext, "test", "123") }) {
            Text(text = "PebbleTest")
        }
    }

    fun sendPebbleMsg(context: Context, title: String, body: String) {
        val i = Intent("com.getpebble.action.SEND_NOTIFICATION")
        val data: Map<String, String> = mapOf("title" to title, "body" to body)
        val jsonData = JSONObject(data)
        val notificationData = JSONArray().put(jsonData).toString()
        i.putExtra("messageType", "PEBBLE_ALERT")
        i.putExtra("sender", "io.github.acedroidx.shark7")
        i.putExtra("notificationData", notificationData)
        Log.d("sendPebbleMsg", notificationData)
        context.sendBroadcast(i)
    }
}