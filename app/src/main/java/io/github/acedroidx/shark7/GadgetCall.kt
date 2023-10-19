package io.github.acedroidx.shark7

import android.content.Context
import android.content.Intent
import android.util.Log

object GadgetCall {
    fun sendGadgetCall(context: Context, msg: String) {
        val i = Intent("nodomain.freeyourgadget.gadgetbridge.command.DEBUG_INCOMING_CALL")
        i.putExtra("caller", msg)
        Log.d("sendGadgetCall", msg)
        context.sendBroadcast(i)
    }
}