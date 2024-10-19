package com.laposa.common.ui.theme

import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.tv.material3.MaterialTheme

@Immutable
object ComponentsTheme {
    @Composable
    fun textInputColors() = TextFieldDefaults.colors().copy(
        unfocusedContainerColor = Color.Transparent,
        focusedContainerColor = Color.Transparent,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedTextColor = Color.White,
        focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        errorContainerColor = Color.Transparent,
        errorTextColor = Color.White,
        errorLabelColor = Color.Red,
        focusedIndicatorColor = Color.White,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}
