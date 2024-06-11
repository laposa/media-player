package ie.laposa.common.features.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ie.laposa.common.features.home.ui.content.HomeContent
import ie.laposa.common.features.menu.ui.Menu

@Composable
fun Home(
    navigateToPlayer: () -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    Row(modifier = Modifier.padding(start = 16.dp, end = 32.dp, top = 32.dp, bottom = 32.dp)) {
        Menu(viewModel::setContent, navigateToPlayer)
        Spacer(modifier = Modifier.width(32.dp))
        Column {
            Spacer(modifier = Modifier.height(56.dp))
            HomeContent()
        }
    }
}