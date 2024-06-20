package ie.laposa.common.features.mediaSource.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.tv.material3.Text
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.MaterialTheme
import ie.laposa.common.theme.surfaceDark
import ie.laposa.common.ui.theme.ComponentsTheme


@Composable
fun MediaSourceLoginDialog(
    mediaSourceName: String,
    error: String?,
    onDismiss: () -> Unit,
    onSubmit: (String?, String?, Boolean) -> Unit,
) {
    var userName by remember { mutableStateOf<String?>("frenkybojler") }
    var password by remember { mutableStateOf<String?>("F98zi6o6") }
    val remember by remember {
        mutableStateOf(true)
    }

    fun onLoginSubmit() {
        onSubmit(userName, password, remember)
    }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()
        ) {
            Card(
                colors = CardDefaults.cardColors().copy(
                    containerColor = surfaceDark,
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.Transparent)
                ) {
                    Text(
                        "Login to $mediaSourceName",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = userName ?: "",
                        onValueChange = { userName = it },
                        label = { Text("User Name") },
                        shape = RoundedCornerShape(12.dp),
                        isError = error != null,
                        colors = ComponentsTheme.textInputColors()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = password ?: "",
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        isError = error != null,
                        colors = ComponentsTheme.textInputColors()
                    )
                    if (error != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(error, color = Color.Red)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onLoginSubmit() },
                        colors = ButtonDefaults.colors(
                            containerColor = Color.White,
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Login")
                    }
                }
            }
        }
    }
}