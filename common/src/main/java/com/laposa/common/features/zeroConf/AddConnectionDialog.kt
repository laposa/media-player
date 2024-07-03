package com.laposa.common.features.zeroConf

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.laposa.common.features.common.composables.selectDialog.SelectDialog
import com.laposa.common.theme.surfaceDark
import com.laposa.common.ui.theme.ComponentsTheme
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceType

@Composable
fun AddConnectionDialog(
    onSubmit: (MediaSource) -> Unit,
    onDismiss: () -> Unit,
) {
    var connectionType by remember {
        mutableStateOf(MediaSourceType.SFTP)
    }

    val scrollState = rememberScrollState()

    var name by remember {
        mutableStateOf<String>("")
    }

    var hostName by remember {
        mutableStateOf<String>("192.168.31.226")
    }
    var userName by remember {
        mutableStateOf<String>("frenkybojler")
    }
    var password by remember {
        mutableStateOf<String>("F98zi6o6")
    }
    var port by remember {
        mutableStateOf<String>("")
    }

    fun addAndConnect() {
        onSubmit(
            MediaSource(
                sourceName = connectionType.name,
                displayName = name,
                connectionAddress = hostName,
                port = if (port.isNotEmpty()) port.toInt() else null,
                username = userName,
                password = password,
                type = connectionType
            )
        )
    }

    BackHandler {
        onDismiss()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors().copy(
                containerColor = surfaceDark,
            ),
            modifier = Modifier
                .width(500.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Add connection",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier = Modifier
                        .background(Color.Transparent)
                        .verticalScroll(scrollState)
                ) {
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("My Home Server") },
                        label = { Text("Connection Name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = ComponentsTheme.textInputColors(),
                        shape = RoundedCornerShape(12.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    SelectDialog(
                        value = connectionType,
                        title = "Connection Type",
                        getOptionTitle = { it.name },
                        options = MediaSourceType.entries
                    ) {
                        connectionType = it
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = hostName,
                        onValueChange = { hostName = it },
                        placeholder = { Text("0.0.0.0") },
                        label = { Text("Host name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = ComponentsTheme.textInputColors(),
                        shape = RoundedCornerShape(12.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = port,
                        onValueChange = { port = it },
                        placeholder = { Text("22") },
                        label = { Text("Port") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = ComponentsTheme.textInputColors(),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.width(100.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("User name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = ComponentsTheme.textInputColors(),
                        shape = RoundedCornerShape(12.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        colors = ComponentsTheme.textInputColors(),
                        shape = RoundedCornerShape(12.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = ::addAndConnect) {
                        Text("Add + Connect")
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}