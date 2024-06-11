package ie.laposa.common.features.menu.ui.menuSections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ie.laposa.common.theme.VideoPlayerTypography

@Composable
fun MenuSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(modifier = modifier) {
        Row(Modifier.padding(start = 16.dp)) {
            Text(title, style = VideoPlayerTypography.titleSmall)
        }
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}