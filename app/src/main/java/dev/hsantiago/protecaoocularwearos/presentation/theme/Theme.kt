package dev.hsantiago.protecaoocularwearos.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material3.ColorScheme
import androidx.wear.compose.material3.MaterialTheme

@Composable
fun ProtecaoOcularWearOSTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColorScheme(
            primary = TealPrimary,
            onPrimary = TealOnPrimary,
            primaryContainer = TealPrimaryContainer,
            onPrimaryContainer = TealOnPrimaryContainer,
            secondary = TealSecondary,
            onSecondary = TealOnSecondary,
            background = TealBackground,
            onBackground = TealOnBackground,
            onSurface = TealOnSurface,
        ),
        content = content
    )
}