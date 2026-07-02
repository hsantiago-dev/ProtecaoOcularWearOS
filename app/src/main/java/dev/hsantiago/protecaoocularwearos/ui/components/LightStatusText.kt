package dev.hsantiago.protecaoocularwearos.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material3.Text
import dev.hsantiago.protecaoocularwearos.model.LightState

@Composable
fun LightStatusText(state: LightState) {
    val (emoji, message) = when (state) {
        LightState.Dark -> "🌙" to "Ambiente escuro — acenda uma luz antes de continuar"
        LightState.Dim -> "🌤️" to "Pouca luz no ambiente"
        LightState.Bright -> "☀️" to "Ambiente adequado"
        LightState.TooBright -> "⚠️" to "Luz muito intensa — pode causar cansaço visual"
    }

    val parts = message.split(" — ", limit = 2)
    val mainText = parts[0]
    val subtitle = parts.getOrNull(1)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = emoji,
            fontSize = 40.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = mainText,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp)
        )
        if (subtitle != null) {
            Text(
                text = subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
