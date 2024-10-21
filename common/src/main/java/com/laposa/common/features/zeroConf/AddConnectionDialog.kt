package com.laposa.common.features.zeroConf

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text
import com.laposa.common.features.common.composables.FocusableForm
import com.laposa.common.features.common.composables.LoadingModal
import com.laposa.common.features.common.composables.MyTextField
import com.laposa.common.features.common.composables.selectDialog.SelectDialog
import com.laposa.common.features.home.ui.LocalSnackbarHost
import com.laposa.common.ui.theme.ComponentsTheme
import com.laposa.domain.mediaSource.model.MediaSource
import com.laposa.domain.mediaSource.model.MediaSourceType
import kotlinx.coroutines.launch


val mediaTypeSettings = mapOf(
    MediaSourceType.SFTP to listOf("connectionName", "hostName", "port", "userName", "password"),
    MediaSourceType.SMB to listOf("connectionName", "hostName", "userName", "password"),
)

@Composable
fun AddConnectionDialog(
    onSubmit: (MediaSource) -> Unit,
    isLoading: Boolean = false,
    error: String?,
    onDismiss: () -> Unit,
) {
    val snackbarHostState = LocalSnackbarHost.current

    val initialFocusRequester = FocusRequester()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(false) {
        initialFocusRequester.requestFocus()
    }

    LaunchedEffect(error) {
        if (error != null) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message = error)
            }
        }
    }

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
        mutableStateOf<String>("laposa")
    }
    var password by remember {
        mutableStateOf<String>("Test1234")
    }
    var port by remember {
        mutableStateOf<String>("22")
    }

    var currentSettings by remember {
        mutableStateOf(mediaTypeSettings[connectionType] ?: emptyList())
    }

    LaunchedEffect(connectionType) {
        currentSettings = mediaTypeSettings[connectionType] ?: emptyList()
    }

    fun addAndConnect() {
        onSubmit(
            MediaSource(
                sourceName = connectionType.name,
                displayName = name,
                connectionAddress = hostName,
                port = port.toInt(),
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
        Surface(
            modifier = Modifier
                .width(500.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(12.dp),
            colors = SurfaceDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)

            ) {
                Text(
                    text = "Add connection",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(16.dp))
                FocusableForm(scrollState, components = listOf(
                    { _, _ -> Spacer(modifier = Modifier.height(16.dp)) },
                    { _, focusNext ->
                        SelectDialog(
                            value = connectionType,
                            title = "Connection Type",
                            getOptionTitle = { it.name },
                            options = listOf(MediaSourceType.SFTP, MediaSourceType.SMB),
                            focusRequester = initialFocusRequester,
                        ) {
                            connectionType = it
                            focusNext()
                        }
                    },
                    { _, _ -> Spacer(modifier = Modifier.height(16.dp)) },
                    { focusRequester, focusNext ->
                        MyTextField(
                            visible = currentSettings.contains("connectionName"),
                            value = name,
                            onValueChange = { name = it },
                            placeholder = { Text("My Home Server") },
                            label = { Text("Connection Name") },
                            shape = RoundedCornerShape(12.dp),
                            colors = ComponentsTheme.textInputColors(),
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(onNext = { focusNext() }),
                            safeFocusRequester = focusRequester,
                        )
                    },
                    { _, _ -> Spacer(modifier = Modifier.height(16.dp)) },
                    { focusRequester, focusNext ->
                        MyTextField(
                            visible = currentSettings.contains("hostName"),
                            value = hostName,
                            onValueChange = { hostName = it },
                            placeholder = { Text("0.0.0.0") },
                            label = { Text("Host name") },
                            colors = ComponentsTheme.textInputColors(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(onNext = { focusNext() }),
                            safeFocusRequester = focusRequester,
                        )
                    },
                    { _, _ -> Spacer(modifier = Modifier.height(16.dp)) },
                    { focusRequester, focusNext ->
                        MyTextField(
                            visible = currentSettings.contains("port"),
                            value = port,
                            onValueChange = { port = it },
                            placeholder = { Text("22") },
                            label = { Text("Port") },
                            colors = ComponentsTheme.textInputColors(),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .width(100.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(onNext = { focusNext() }),
                            safeFocusRequester = focusRequester,
                        )
                    },
                    { _, _ -> Spacer(modifier = Modifier.height(16.dp)) },
                    { focusRequester, focusNext ->
                        MyTextField(
                            visible = currentSettings.contains("userName"),
                            value = userName,
                            onValueChange = { userName = it },
                            label = { Text("User name") },
                            colors = ComponentsTheme.textInputColors(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(onNext = { focusNext() }),
                            safeFocusRequester = focusRequester,
                        )
                    },
                    { _, _ -> Spacer(modifier = Modifier.height(16.dp)) },
                    { focusRequester, focusNext ->
                        MyTextField(
                            visible = currentSettings.contains("password"),
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            colors = ComponentsTheme.textInputColors(),
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(onNext = { focusNext() }),
                            safeFocusRequester = focusRequester,
                        )
                    },
                    { _, _ -> Spacer(modifier = Modifier.height(32.dp)) },
                    { focusRequester, _ ->
                        Row {
                            Button(
                                onClick = ::addAndConnect,
                                modifier = Modifier.focusRequester(focusRequester.focusRequester),
                                border = ButtonDefaults.border(
                                    border = Border(
                                        border = BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.border
                                        )
                                    ),
                                )
                            ) {
                                Text("Add & Connect")
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                onClick = onDismiss,
                                border = ButtonDefaults.border(
                                    border = Border(
                                        border = BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.border
                                        )
                                    ),
                                )
                            ) {
                                Text("Cancel")
                            }
                        }
                    },
                    { _, _ -> Spacer(modifier = Modifier.height(24.dp)) }
                ))
            }
            LoadingModal(isLoading)
        }
    }
}

