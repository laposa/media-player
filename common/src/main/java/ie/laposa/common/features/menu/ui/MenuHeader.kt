package ie.laposa.common.features.menu.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import ie.laposa.common.R
import ie.laposa.common.theme.VideoPlayerTypography

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painterResource(R.drawable.video_player_icon),
            contentDescription = "Video Player",
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            "Laposa Player",
            color = Color.White,
            style = VideoPlayerTypography.headlineSmall.copy(
                fontSize = TextUnit(
                    18f,
                    TextUnitType.Sp
                )
            )
        )
    }
}