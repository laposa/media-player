package ie.laposa.common.features.mediaSource.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


@Composable
fun MediaSourceLoginDialog(onSubmit: (String, String) -> Unit) {
    var userName by remember { mutableStateOf("frenkybojler") }
    var password by remember { mutableStateOf("F98zi6o6") }

    Dialog(onDismissRequest = { }) {
        Card {
            Box(modifier = Modifier.padding(16.dp)) {
                Column {
                    Text("Login to media source")

                    TextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("User Name") }
                    )

                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    Button(onClick = { onSubmit(userName, password) }) {
                        Text("Login")
                    }
                }
            }
        }
    }
}