package ie.laposa.common.features.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.laposa.common.features.menu.ui.Menu

@Composable
fun Home() {
    Row(modifier = Modifier.padding(start = 16.dp, end = 32.dp, top = 32.dp, bottom = 32.dp)) {
        Menu()
        Spacer(modifier = Modifier.width(32.dp))
        Column {
        }
    }
}