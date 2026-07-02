package dev.hsantiago.protecaoocularwearos.model

data class LightReading(
    val lux: Float,
    val timestamp: Long = System.currentTimeMillis()
)
