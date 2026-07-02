package dev.hsantiago.protecaoocularwearos.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.hsantiago.protecaoocularwearos.ui.components.LightRingIndicator
import dev.hsantiago.protecaoocularwearos.ui.components.LightStatusText
import dev.hsantiago.protecaoocularwearos.ui.components.MonitoringToggleChip
import dev.hsantiago.protecaoocularwearos.viewmodel.LightViewModel
import androidx.wear.compose.material3.Text
import dev.hsantiago.protecaoocularwearos.presentation.theme.TealBackground
import dev.hsantiago.protecaoocularwearos.presentation.theme.TealSecondary

@Composable
fun HomeScreen(viewModel: LightViewModel) {
    val lux by viewModel.luxAtual.collectAsState()
    val state by viewModel.estadoAtual.collectAsState()
    val isMonitoring by viewModel.isMonitoring.collectAsState()

    val luxTransition = rememberInfiniteTransition(label = "luxPulse")
    val luxAlpha by luxTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "luxAlpha"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize().background(TealBackground)
    ) {
        LightRingIndicator(state = state, isMonitoring = isMonitoring)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .then(if (!isMonitoring) Modifier.alpha(0.4f) else Modifier)
        ) {
            LightStatusText(state = state)

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${lux.toInt()} lux",
                fontSize = 14.sp,
                color = TealSecondary.copy(alpha = if (isMonitoring) luxAlpha else 1f),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            MonitoringToggleChip(
                isMonitoring = isMonitoring,
                onToggle = { viewModel.toggleMonitoring() }
            )
        }
    }
}
