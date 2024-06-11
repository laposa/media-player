package ie.laposa.common.features.menu.ui.menuSections

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import ie.laposa.common.features.home.ui.content.Dashboard

@Composable
fun HomeMenuSection(setHomeContent: (@Composable () -> Unit) -> Unit) {
    MenuSection("Home") {
        SectionItem(
            "Dashboard",
            icon = Icons.Filled.Home,
            focusedByDefault = true,
        ) {
            setHomeContent {
                Dashboard()
            }
        }
    }
}