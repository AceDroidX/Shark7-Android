package io.github.acedroidx.shark7.ui.compose

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class FcmTokenInfo(val token: MutableState<String>) {
    fun getToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FcmTokenInfo", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val result = task.result

            Log.d("FcmTokenInfo", "Token:$result")
            token.value = result
        })
    }

    companion object {
        @Composable
        fun Composable() {
            val state = rememberState()
            LaunchedEffect(Unit) {
                state.getToken()
            }
            Text(
                "Token: ${state.token.value}\n",
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        @Composable
        fun rememberState(token: MutableState<String> = rememberSaveable { mutableStateOf("") }) =
            remember {
                FcmTokenInfo(token)
            }
    }
}