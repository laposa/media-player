package com.laposa.common.features.common.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.MaterialTheme.colorScheme

@Composable
fun MyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    visible: Boolean = true,
    safeFocusRequester: SafeFocusRequester? = null,
    focusedByDefault: Boolean = false,
) {

    val focusRequester = remember {
        safeFocusRequester?.focusRequester ?: FocusRequester()
    }

    var isFocused by remember {
        mutableStateOf(false)
    }

    val innerFocusRequester = remember { FocusRequester() }

    if (!visible) {
        safeFocusRequester?.reset()
        return
    }

    LaunchedEffect(focusRequester) {
        if (focusedByDefault) {
            focusRequester.requestFocus()
        }
    }

    Card(
        modifier =
            Modifier
                .height(70.dp)
                .focusRequester(focusRequester)
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .testTag("text_field_${label?.toString()}"),
        colors = CardDefaults.colors(
            containerColor = Color.Transparent,
        ),
        onClick = { innerFocusRequester.requestFocus() },
        border = CardDefaults.border(
            border = if (!isFocused) {
                Border(
                    border = BorderStroke(
                        1.dp,
                        colorScheme.border
                    )
                )
            } else Border(
                border = BorderStroke(
                    2.dp,
                    Color.White,
                )
            ),
            focusedBorder = Border(
                border = BorderStroke(
                    2.dp,
                    Color.White,
                )
            )
        ),
        scale = CardDefaults.scale(focusedScale = 1.025f),
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .focusRequester(innerFocusRequester)
                .onFocusChanged {
                    if(it.isFocused) {
                        isFocused = true
                    }
                },
            enabled = enabled,
            readOnly = readOnly,
            textStyle = textStyle,
            label = label,
            placeholder = placeholder,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix,
            supportingText = supportingText,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors.copy(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}