package ie.laposa.common.features.menu.ui.menuSections

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ie.laposa.common.theme.VideoPlayerTypography
import androidx.compose.material3.Icon as IconCompose

@Composable
fun SectionItem(
    title: String,
    subTitle: String? = null,
    iconResource: Int? = null,
    icon: ImageVector? = null,
    isLoading: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    var isFocused by remember { mutableStateOf(isSelected) }
    val focusRequester = remember { FocusRequester() }

    var focusedFromInside by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isSelected) {
            focusRequester.requestFocus()
        }
    }

    Surface(
        shape = RoundedCornerShape(3.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp)
            .onFocusEvent {
                isFocused = it.hasFocus
                if (it.hasFocus && !focusedFromInside) {
                    focusedFromInside = true
                    onClick()
                } else if (!it.hasFocus) {
                    focusedFromInside = false
                }
            }
            .focusable()
            .focusRequester(focusRequester)
            .clickable { onClick() }
            .fillMaxWidth(),
        color = if (isSelected) Color.White else Color.Transparent
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 4.dp)
        ) {
            Row {
                if (iconResource != null) {
                    IconCompose(
                        painter = painterResource(iconResource),
                        contentDescription = title,
                        modifier = Modifier
                            .width(14.dp)
                            .height(14.dp),
                        tint = if (isSelected) Color.Black else Color.White,
                    )
                } else if (icon != null) {
                    IconCompose(
                        icon,
                        contentDescription = title,
                        modifier = Modifier
                            .width(14.dp)
                            .height(14.dp),
                        tint = if (isSelected) Color.Black else Color.White,
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                if (subTitle != null) {
                    Text(
                        subTitle,
                        modifier = Modifier.width(30.dp),
                        style = VideoPlayerTypography.bodySmall.copy(
                            color = if (isSelected) Color.Black else Color.White
                        ),
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                title,
                style = VideoPlayerTypography.labelMedium.copy(
                    color = if (isSelected) Color.Black else Color.White
                ),
            )
            Spacer(modifier = Modifier.weight(1f))
            if (isLoading) CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                color = if (isSelected) Color.Black else Color.White,
                strokeWidth = 2.dp
            )
        }
    }
}