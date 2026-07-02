package dev.hsantiago.protecaoocularwearos.model

object LightClassifier {
    fun classify(lux: Float): LightState = when {
        lux < 10f -> LightState.Dark
        lux < 300f -> LightState.Dim
        lux < 1000f -> LightState.Bright
        else -> LightState.TooBright
    }
}
