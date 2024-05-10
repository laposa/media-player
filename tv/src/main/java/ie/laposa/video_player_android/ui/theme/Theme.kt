package ie.laposa.video_player_android.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.darkColorScheme
import androidx.tv.material3.lightColorScheme
import ie.laposa.video_player_android.ui.colorSchemeForDarkMode
import ie.laposa.video_player_android.ui.colorSchemeForLightMode

@Composable
fun LaposaVideoPlayerTheme(
    isInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isInDarkTheme) {
        colorSchemeForDarkMode
    } else {
        colorSchemeForLightMode
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}