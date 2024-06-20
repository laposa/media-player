package ie.laposa.common.features.menu.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.tv.material3.DrawerValue
import androidx.tv.material3.Icon
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.NavigationDrawerItem
import androidx.tv.material3.Text
import androidx.tv.material3.rememberDrawerState
import ie.laposa.common.R
import ie.laposa.common.features.home.ui.LocalHomeNavigation
import ie.laposa.common.features.home.ui.content.HomeContent

data class MenuSection(
    val title: String,
    val items: List<MenuItem> = emptyList(),
    val onClick: (MenuSection) -> Unit,
    val iconResource: Int? = null,
    val icon: ImageVector? = null,
)

data class MenuItem(
    val title: String,
    val onClick: (MenuItem) -> Unit,
    val subTitle: String? = null,
    val iconResource: Int? = null,
    val icon: ImageVector? = null,
    val isLoading: Boolean = false,
)

@Composable
fun Menu() {
    val firstItemFocusRequester = FocusRequester()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)

    var selectedIndex by remember { mutableIntStateOf(0) }
    val homeNavigation = LocalHomeNavigation.current

    val menuStructure = listOf(
        MenuSection(
            title = "Home", icon = Icons.Outlined.Home,
            onClick = {
                homeNavigation.navigateToHome()
            },
        ),
        MenuSection(
            title = "Network", iconResource = R.drawable.dns,
            onClick = {
                homeNavigation.navigateToZeroConf()
            },
        ),
        MenuSection(
            title = "Local Storage", iconResource = R.drawable.ic_storage,
            onClick = {
                homeNavigation.navigateToLocalStorage()
            },
        ),
    )

    LaunchedEffect(true) {
        firstItemFocusRequester.requestFocus()
    }

    fun onSelectMenuSection(section: MenuSection, index: Int) {
        selectedIndex = index
        section.onClick(section)
    }

    Column {
        Header()
        Spacer(modifier = Modifier.height(32.dp))
        NavigationDrawer(drawerState = drawerState, drawerContent = {
            Column(
                Modifier
                    .fillMaxHeight()
                    .padding(12.dp)
                    .selectableGroup(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                menuStructure.forEachIndexed { rootIndex, section ->
                    NavigationDrawerItem(
                        selected = selectedIndex == rootIndex,
                        onClick = {
                            onSelectMenuSection(section, rootIndex)
                        },
                        leadingContent = {
                            if (section.iconResource != null) {
                                Icon(
                                    painter = painterResource(section.iconResource),
                                    contentDescription = section.title,
                                )
                            } else if (section.icon != null) {
                                Icon(
                                    section.icon,
                                    contentDescription = section.title,
                                )
                            }
                        },
                        content = {
                            Text(
                                section.title, maxLines = 1, overflow = TextOverflow.Ellipsis
                            )
                        },
                        modifier = Modifier.focusRequester(if (rootIndex == 0) firstItemFocusRequester else FocusRequester())
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                NavigationDrawerItem(selected = selectedIndex == menuStructure.size, onClick = {
                    selectedIndex = menuStructure.size
                    homeNavigation.navigateToSettings()
                }, leadingContent = {
                    Icon(
                        Icons.Outlined.Settings,
                        contentDescription = "Settings",
                    )
                }, content = {
                    Text(
                        "Settings", maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                })
            }

        }) {
            Row {
                Spacer(modifier = Modifier.width(32.dp))
                HomeContent()
            }
        }
    }
}

