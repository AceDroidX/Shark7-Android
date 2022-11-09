package io.github.acedroidx.shark7.ui

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import io.github.acedroidx.shark7.ui.compose.SubscribeTopic
import io.github.acedroidx.shark7.ui.theme.Shark7Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SubscribeTopic.subscribeTopic("main")
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        setContent {
            Shark7Theme {
                Scaffold(topBar = { TopAppBar(title = { Text("Shark7") }) }) { contentPadding ->
                    // Screen content
                    Box(
                        modifier = Modifier
                            .padding(contentPadding)
                            .verticalScroll(rememberScrollState())
                            .scrollable(
                                orientation = Orientation.Vertical,
                                state = rememberScrollableState { delta -> 0f })
                    ) {
                        MainContent()
                    }
                }
            }
        }
    }

    @Composable
    fun MainContent() {
        val enableAlarm by viewModel.enableAlarm.collectAsState(initial = false)
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Enable Alarm",
                    color = MaterialTheme.colorScheme.onBackground
                )
                Switch(
                    checked = enableAlarm,
                    onCheckedChange = { viewModel.setEnableAlarm(it) })
            }
            Button(onClick = { viewModel.openAlarmScopeActivity(this@MainActivity) }) {
                Text(text = "Open AlarmScopeActivity")
            }
            Button(onClick = { viewModel.openDebugActivity(this@MainActivity) }) {
                Text(text = "Open DebugActivity")
            }
        }
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }
}