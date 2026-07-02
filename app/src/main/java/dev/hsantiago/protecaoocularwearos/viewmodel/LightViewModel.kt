package dev.hsantiago.protecaoocularwearos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.hsantiago.protecaoocularwearos.model.LightClassifier
import dev.hsantiago.protecaoocularwearos.model.LightReading
import dev.hsantiago.protecaoocularwearos.model.LightState
import dev.hsantiago.protecaoocularwearos.sensor.LightSensorManager
import dev.hsantiago.protecaoocularwearos.util.VibrationHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LightViewModel(
    private val sensorManager: LightSensorManager,
    private val vibrationHelper: VibrationHelper
) : ViewModel() {

    private val _isMonitoring = MutableStateFlow(true)
    val isMonitoring: StateFlow<Boolean> = _isMonitoring.asStateFlow()

    private val _luxAtual = MutableStateFlow(0f)
    val luxAtual: StateFlow<Float> = _luxAtual.asStateFlow()

    private val _estadoAtual = MutableStateFlow<LightState>(LightState.Dim)
    val estadoAtual: StateFlow<LightState> = _estadoAtual.asStateFlow()

    private val _historico = MutableStateFlow<List<LightReading>>(emptyList())
    val historico: StateFlow<List<LightReading>> = _historico.asStateFlow()

    init {
        observeSensor()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeSensor() {
        viewModelScope.launch {
            _isMonitoring
                .flatMapLatest { monitoring ->
                    if (monitoring) sensorManager.observeLightSensor()
                    else emptyFlow()
                }
                .collect { lux ->
                    onLuxReading(lux)
                }
        }
    }

    private fun onLuxReading(lux: Float) {
        _luxAtual.value = lux
        _estadoAtual.value = LightClassifier.classify(lux)
    }

    fun toggleMonitoring() {
        _isMonitoring.value = !_isMonitoring.value
    }
}
