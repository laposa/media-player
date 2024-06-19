package ie.laposa.common.features.menu.ui.menuSections

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import ie.laposa.common.features.home.ui.content.Dashboard
import ie.laposa.domain.mediaSource.model.MediaSourceFile

@Composable
fun HomeMenuSection(
    key: String,
    selectedKey: String?,
    onSelected: (key: String) -> Unit,
    setHomeContent: (@Composable () -> Unit) -> Unit,
    onNavigateToPlayer: (MediaSourceFile?, String?) -> Unit
) {
    MenuSection("Home") {
        SectionItem(
            "Dashboard",
            icon = Icons.Filled.Home,
            isSelected = selectedKey == key,
        ) {
            onSelected(key)
            setHomeContent {
                Dashboard(onNavigateToPlayer)
            }
        }
    }
}