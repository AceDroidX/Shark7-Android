package io.github.acedroidx.shark7.ui.compose

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

object SubscribeTopic {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Composable(enabled: Boolean = false) {
        var topic by remember { mutableStateOf("") }
        Column {
            OutlinedTextField(
                value = topic,
                label = { Text("topic") },
                onValueChange = { topic = it })
            Button(onClick = { subscribeTopic(topic) }, enabled = enabled) {
                Text(text = "subscribeTopic")
            }
        }
    }

    fun subscribeTopic(topic: String) {
        Firebase.messaging.subscribeToTopic(topic).addOnCompleteListener { task ->
            var msg = "Subscribed"
            if (!task.isSuccessful) {
                msg = "Subscribe failed"
            }
            Log.d("SubscribeTopic", "subscribeTopic:$msg")
        }
    }
}