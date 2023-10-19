package io.github.acedroidx.shark7.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import io.github.acedroidx.shark7.model.Scope
import io.github.acedroidx.shark7.ui.theme.Shark7Theme

@AndroidEntryPoint
class AlarmScopeActivity : ComponentActivity() {
    private val viewModel: AlarmScopeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val alarmScope by viewModel.alarmScope.collectAsState(initial = emptySet())
            Shark7Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScopeList(alarmScope) { id, value -> viewModel.setAlarmScope(id, value) }
                }
            }
        }
    }
}

@Composable
fun ScopeList(set: Set<String>, onChecked: (String, Boolean) -> Unit) {
    LazyColumn {
        items(Scope.values()) { item ->
            Row {
                Checkbox(
                    checked = set.contains(item.id),
                    onCheckedChange = { onChecked(item.id, it) })
                Text(text = "${item.desc}(${item.id})")
            }
        }
    }
}

@Composable
fun Greeting3(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview3() {
    Shark7Theme {
        Greeting3("Android")
    }
}