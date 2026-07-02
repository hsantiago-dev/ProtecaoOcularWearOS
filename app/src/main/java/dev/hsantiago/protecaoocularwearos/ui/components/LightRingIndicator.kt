package dev.hsantiago.protecaoocularwearos.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.ProgressIndicatorColors
import dev.hsantiago.protecaoocularwearos.model.LightState
import dev.hsantiago.protecaoocularwearos.presentation.theme.StateBright
import dev.hsantiago.protecaoocularwearos.presentation.theme.StateDark
import dev.hsantiago.protecaoocularwearos.presentation.theme.StateDim
import dev.hsantiago.protecaoocularwearos.presentation.theme.StateTooBright

@Composable
fun LightRingIndicator(state: LightState, isMonitoring: Boolean) {
    val color by animateColorAsState(
        targetValue = when (state) {
            LightState.Dark -> StateDark
            LightState.Dim -> StateDim
            LightState.Bright -> StateBright
            LightState.TooBright -> StateTooBright
        },
        label = "ringColor"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ringAlpha"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            progress = { 1f },
            modifier = Modifier.size(260.dp),
            strokeWidth = 6.dp,
            colors = ProgressIndicatorColors(
                indicatorBrush = SolidColor(color.copy(alpha = if (isMonitoring) ringAlpha else 0.6f)),
                trackBrush = SolidColor(Color.Transparent),
                overflowTrackBrush = SolidColor(Color.Transparent),
                disabledIndicatorBrush = SolidColor(color.copy(alpha = 0.3f)),
                disabledTrackBrush = SolidColor(Color.Transparent),
                disabledOverflowTrackBrush = SolidColor(Color.Transparent)
            )
        )
    }
}
