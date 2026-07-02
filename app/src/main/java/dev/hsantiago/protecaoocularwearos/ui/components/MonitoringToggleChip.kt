package dev.hsantiago.protecaoocularwearos.ui.components

import androidx.compose.runtime.Composable
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Text

@Composable
fun MonitoringToggleChip(
    isMonitoring: Boolean,
    onToggle: () -> Unit
) {
    Button(onClick = onToggle) {
        Text(
            text = if (isMonitoring) "Monitorando" else "Pausado"
        )
    }
}
