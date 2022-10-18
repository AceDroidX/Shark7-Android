package io.github.acedroidx.shark7

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class MyBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val intentService = Intent(
            context,
            AlarmService::class.java
        )
        context.stopService(intentService)
    }
}