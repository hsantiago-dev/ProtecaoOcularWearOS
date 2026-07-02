package dev.hsantiago.protecaoocularwearos.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.Text

@Composable
fun MonitoringToggleChip(
    isMonitoring: Boolean,
    onToggle: () -> Unit
) {
    Button(onClick = onToggle, modifier = Modifier.height(40.dp).padding(bottom = 8.dp)) {
        Text(
            text = if (isMonitoring) "Monitorando" else "Pausado"
        )
    }
}
