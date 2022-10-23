package io.github.acedroidx.shark7.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import io.github.acedroidx.shark7.ui.theme.Shark7Theme

@AndroidEntryPoint
class DebugActivity : ComponentActivity() {
    private val viewModel: DebugViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getToken()
        viewModel.getRouteInfo(baseContext)
        viewModel.getAudioDeviceInfo(baseContext)
        setContent {
            val token by viewModel.token.observeAsState()
            val routeInfo by viewModel.routeInfo.observeAsState()
            val audioDeviceInfo by viewModel.audioDevicesInfo.observeAsState()
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
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Column() {
                                Infos(token, routeInfo, audioDeviceInfo)
                                Button(onClick = {
                                    viewModel.getAudioDeviceInfo(baseContext);viewModel.getRouteInfo(
                                    baseContext
                                )
                                }) {
                                    Text(text = "Refresh Audio Devices")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting2(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    Shark7Theme {
        Greeting2("Android")
    }
}

@Composable
fun Infos(token: String?, routeInfo: String?, audioDeviceInfo: String?) {
    SelectionContainer {
        Column {
            Text(
                "Token: ${token}\n",
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "RouteInfo: ${routeInfo}\n",
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "AudioDeviceInfo: ${audioDeviceInfo}",
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}