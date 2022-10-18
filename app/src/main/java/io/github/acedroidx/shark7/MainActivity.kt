package io.github.acedroidx.shark7

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
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import io.github.acedroidx.shark7.ui.theme.Shark7Theme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        viewModel.getToken()
        viewModel.getRouteInfo(baseContext)
        viewModel.getAudioDeviceInfo(baseContext)
        setContent {
            val token = viewModel.token.observeAsState()
            val routeInfo = viewModel.routeInfo.observeAsState()
            val audioDeviceInfo = viewModel.audioDevicesInfo.observeAsState()
            var topic by remember { mutableStateOf("") }
            var audioAttr by remember { mutableStateOf(MyAudioAttributes.USAGE_ASSISTANT) }
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
                        Column {
                            Greeting("Android")
                            SelectionContainer {
                                Column {
                                    Text(
                                        "Token: ${token.value}\n",
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        "RouteInfo: ${routeInfo.value}\n",
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Text(
                                        "AudioDeviceInfo: ${audioDeviceInfo.value}",
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }
                            }
                            OutlinedTextField(
                                value = topic,
                                label = { Text("topic") },
                                onValueChange = { topic = it })
                            Button(onClick = { viewModel.subscribeTopic(topic) }) {
                                Text(text = "subscribeTopic")
                            }
                            Row {
                                Button(onClick = { viewModel.startService(baseContext,audioAttr) }) {
                                    Text(text = "StartService")
                                }
                                Button(onClick = { viewModel.stopService(baseContext) }) {
                                    Text(text = "StopService")
                                }
                            }
                            Button(onClick = {
                                viewModel.getAudioDeviceInfo(baseContext);viewModel.getRouteInfo(
                                baseContext
                            )
                            }) {
                                Text(text = "Refresh Audio Devices")
                            }
                            audioAttrCompose(audioAttr) { audioAttr = it }
                        }
                    }
                }
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

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Shark7Theme {
        Greeting("Android")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun audioAttrCompose(audioAttr: MyAudioAttributes, onSelected: (MyAudioAttributes) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = audioAttr.name,
            onValueChange = {},
            label = { Text("AudioAttributes") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            // colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            MyAudioAttributes.values().forEach { selectionOption ->
                DropdownMenuItem(
                    text = { Text(selectionOption.name) },
                    onClick = {
                        onSelected(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}