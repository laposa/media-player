package ie.laposa.common.features.menu.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.laposa.common.features.menu.ui.menuSections.HomeMenuSection
import ie.laposa.common.features.menu.ui.menuSections.ZeroConfMenuSection

@Composable
fun Menu(setHomeContent: (@Composable () -> Unit) -> Unit, navigateToPlayer: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth(0.25f)) {
        Box(modifier = Modifier.padding(start = 16.dp)) {
            Header()
        }
        Spacer(modifier = Modifier.height(32.dp))
        Column {
            HomeMenuSection(setHomeContent, navigateToPlayer)
            Spacer(modifier = Modifier.height(16.dp))
            ZeroConfMenuSection(setHomeContent, navigateToPlayer)
        }
    }
}

