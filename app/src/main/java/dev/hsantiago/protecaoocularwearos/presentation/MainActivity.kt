package dev.hsantiago.protecaoocularwearos.presentation

import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.hsantiago.protecaoocularwearos.presentation.theme.ProtecaoOcularWearOSTheme
import dev.hsantiago.protecaoocularwearos.sensor.LightSensorManager
import dev.hsantiago.protecaoocularwearos.ui.screens.HomeScreen
import dev.hsantiago.protecaoocularwearos.util.VibrationHelper
import dev.hsantiago.protecaoocularwearos.viewmodel.LightViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val lightSensorManager = LightSensorManager(sensorManager)
        val vibrationHelper = VibrationHelper(this)

        val viewModel = ViewModelProvider(
            owner = this,
            factory = object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LightViewModel(lightSensorManager, vibrationHelper) as T
                }
            }
        )[LightViewModel::class.java]

        setContent {
            ProtecaoOcularWearOSTheme {
                HomeScreen(viewModel = viewModel)
            }
        }
    }
}
