package com.laposa.common.features.player.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import ie.laposa.common.R

@Composable
fun VlcPlayerControls(
    visible: Boolean,
    title: String,
    isPlaying: Boolean,
    positionMs: Long,
    durationMs: Long,
    onPlayPause: () -> Unit,
    onSeekBack: () -> Unit,
    onSeekForward: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val playPauseFocusRequester = remember { FocusRequester() }

    LaunchedEffect(visible) {
        if (visible) {
            runCatching { playPauseFocusRequester.requestFocus() }
        }
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0xEE000000))
                        )
                    )
                    .padding(horizontal = 40.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Progress bar + time stamps
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    LinearProgressIndicator(
                        progress = {
                            if (durationMs > 0)
                                (positionMs.toFloat() / durationMs).coerceIn(0f, 1f)
                            else 0f
                        },
                        modifier = Modifier.weight(1f).height(4.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = Color.White.copy(alpha = 0.3f),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "${formatTime(positionMs)} / ${formatTime(durationMs)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f),
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Control buttons
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    PlayerControlButton(
                        iconRes = R.drawable.ic_fast_rewind,
                        label = "-10s",
                        onClick = onSeekBack,
                        iconSize = 28.dp,
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    PlayerControlButton(
                        iconRes = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play,
                        label = if (isPlaying) "Pause" else "Play",
                        onClick = onPlayPause,
                        iconSize = 40.dp,
                        modifier = Modifier.focusRequester(playPauseFocusRequester),
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    PlayerControlButton(
                        iconRes = R.drawable.ic_fast_forward,
                        label = "+10s",
                        onClick = onSeekForward,
                        iconSize = 28.dp,
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayerControlButton(
    iconRes: Int,
    label: String,
    onClick: () -> Unit,
    iconSize: Dp = 28.dp,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = ButtonDefaults.shape(shape = RoundedCornerShape(12.dp)),
        border = ButtonDefaults.border(
            border = Border(
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.border)
            ),
            focusedBorder = Border(
                border = BorderStroke(2.dp, Color.White)
            ),
        ),
        colors = ButtonDefaults.colors(
            containerColor = Color.Black.copy(alpha = 0.4f),
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f),
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(iconSize),
                tint = Color.White,
            )
        }
    }
}

private fun formatTime(ms: Long): String {
    if (ms <= 0) return "0:00"
    val totalSec = ms / 1000
    val hours = totalSec / 3600
    val minutes = (totalSec % 3600) / 60
    val seconds = totalSec % 60
    return if (hours > 0)
        "%d:%02d:%02d".format(hours, minutes, seconds)
    else
        "%d:%02d".format(minutes, seconds)
}
