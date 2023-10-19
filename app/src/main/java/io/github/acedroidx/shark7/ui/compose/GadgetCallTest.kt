package io.github.acedroidx.shark7.ui.compose

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.github.acedroidx.shark7.GadgetCall.sendGadgetCall


object GadgetCallTest {
    @Composable
    fun Composable() {
        val baseContext = LocalContext.current
        Button(onClick = { sendGadgetCall(baseContext, "test") }) {
            Text(text = "GadgetCallTest")
        }
    }
}