package ie.laposa.common.features.menu.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.laposa.common.features.menu.ui.menuSections.HomeMenuSection
import ie.laposa.common.features.menu.ui.menuSections.ZeroConfMenuSection

const val HOME_SECTION_KEY = "home"
const val ZERO_CONF_SECTION_KEY = "zeroconf"

@Composable
fun Menu(setHomeContent: (@Composable () -> Unit) -> Unit, navigateToPlayer: () -> Unit) {
    var selectedMenuItem: String? by remember {
        mutableStateOf(HOME_SECTION_KEY)
    }

    fun onSelected(key: String) {
        println("SELECTED MENU ITEM: $key")
        selectedMenuItem = key
    }

    Column(modifier = Modifier.fillMaxWidth(0.25f)) {
        Box(modifier = Modifier.padding(start = 16.dp)) {
            Header()
        }
        Spacer(modifier = Modifier.height(32.dp))
        Column {
            HomeMenuSection(
                key = HOME_SECTION_KEY,
                onSelected = ::onSelected,
                selectedKey = selectedMenuItem,
                setHomeContent = setHomeContent,
                onNavigateToPlayer = navigateToPlayer
            )
            Spacer(modifier = Modifier.height(16.dp))
            ZeroConfMenuSection(
                key = ZERO_CONF_SECTION_KEY,
                onSelected = ::onSelected,
                selectedKey = selectedMenuItem,
                setHomeContent = setHomeContent,
                navigateToPlayer = navigateToPlayer,
            )
        }
    }
}

