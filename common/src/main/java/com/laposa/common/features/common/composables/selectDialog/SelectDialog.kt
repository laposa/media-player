package com.laposa.common.features.common.composables.selectDialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.laposa.common.theme.onSurfaceDark
import com.laposa.common.theme.surfaceDark
import ie.laposa.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectDialog(
    value: T,
    title: String,
    getOptionTitle: (T) -> String,
    options: List<T>,
    onOptionSelected: (T) -> Unit,
) {
    val focusRequester = FocusRequester()
    val connectionTypeDialogFocusRequester = FocusRequester()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isDialogVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isDialogVisible) {
        if (isDialogVisible) {
            connectionTypeDialogFocusRequester.requestFocus()
        } else {
            focusRequester.requestFocus()
        }
    }

    Card(
        modifier = Modifier
            .height(70.dp)
            .focusRequester(focusRequester),
        onClick = { isDialogVisible = true },
        colors = CardDefaults.cardColors().copy(
            containerColor = surfaceDark,
        ),
        border = BorderStroke(1.dp, onSurfaceDark)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Column {
                Text(text = title, color = Color.White, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = getOptionTitle(value),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(id = if (isDialogVisible) R.drawable.ic_arrow_drop_up else R.drawable.ic_arrow_drop_down),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
    }

    if (!isDialogVisible) return

    ModalBottomSheet(
        onDismissRequest = { isDialogVisible = false },
        containerColor = surfaceDark,
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(32.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            options.forEachIndexed { index, it ->
                ListItem(selected = false,
                    headlineContent = { Text(getOptionTitle(it)) },
                    modifier = if (index == 0) Modifier.focusRequester(
                        connectionTypeDialogFocusRequester
                    ) else Modifier,
                    onClick = {
                        onOptionSelected(it)
                        isDialogVisible = false
                    })
            }
        }
    }
}