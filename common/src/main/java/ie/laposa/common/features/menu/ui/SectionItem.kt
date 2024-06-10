package ie.laposa.common.features.menu.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ie.laposa.common.theme.VideoPlayerTypography

@Composable
fun SectionItem(title: String, icon: Int, onClick: () -> Unit, isLoading: Boolean = false) {
    var isFocused by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .onFocusEvent {
                println("onFocusEvent ${it.hasFocus}")
                isFocused = it.hasFocus
            }
            .focusable()
            .clickable { onClick() }.fillMaxWidth(),
        color = if (isFocused) Color.White else Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = title,
                modifier = Modifier.width(14.dp).height(14.dp),
                tint = if (isFocused) Color.Black else Color.White,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                title,
                style = VideoPlayerTypography.labelMedium.copy(
                    color = if (isFocused) Color.Black else Color.White
                ),
            )
            Spacer(modifier = Modifier.weight(1f))
            if (isLoading) CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                color = if (isFocused) Color.Black else Color.White,
                strokeWidth = 2.dp
            )
        }
    }
}