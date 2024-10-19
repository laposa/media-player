package com.laposa.common.features.home.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.Surface
import com.laposa.common.features.menu.ui.Menu
import com.laposa.common.features.player.ui.PlayerScreen
import com.laposa.common.ui.rememberSnackbarHostState

@Composable
fun Home(
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val homeNavController = rememberNavController()

    val homeNavigation = remember {
        HomeNavigation(homeNavController, viewModel::showPlayer)
    }

    val fileToPlay = viewModel.fileToPlay.collectAsState().value

    val snackbarHostState = rememberSnackbarHostState()

    ProvideHomeSnackbarHost(snackbarHostState = snackbarHostState) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.surfaceVariant,
        ) { paddingValues ->
            ProvideHomeNavController(homeNavigation = homeNavigation) {
                Surface(
                    modifier = Modifier.padding(
                        paddingValues
                    )
                ) {
                    Box(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 32.dp,
                            top = 32.dp,
                            bottom = 32.dp
                        )
                    ) {
                        Menu()
                        if (fileToPlay != null) {
                            PlayerScreen(
                                fileToPlay,
                                viewModel::hidePlayer,
                            )
                        }
                    }

                }
            }
        }
    }
}