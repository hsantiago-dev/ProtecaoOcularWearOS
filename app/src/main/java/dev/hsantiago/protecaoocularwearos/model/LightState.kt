package dev.hsantiago.protecaoocularwearos.model

sealed class LightState {
    data object Dark : LightState()
    data object Dim : LightState()
    data object Bright : LightState()
    data object TooBright : LightState()
}
