package io.github.acedroidx.shark7.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import io.github.acedroidx.shark7.ui.compose.AudioInfo
import io.github.acedroidx.shark7.ui.compose.FcmTokenInfo
import io.github.acedroidx.shark7.ui.compose.PebbleTest
import io.github.acedroidx.shark7.ui.compose.ServiceTest
import io.github.acedroidx.shark7.ui.compose.SubscribeTopic
import io.github.acedroidx.shark7.ui.theme.Shark7Theme

@AndroidEntryPoint
class DebugActivity : ComponentActivity() {
    //    private val viewModel: DebugViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Column {
                                ServiceTest.Composable()
                                FcmTokenInfo.Composable()
                                AudioInfo.Composable()
                                SubscribeTopic.Composable()
                                PebbleTest.Composable()
                            }
                        }
                    }
                }
            }
        }
    }
}