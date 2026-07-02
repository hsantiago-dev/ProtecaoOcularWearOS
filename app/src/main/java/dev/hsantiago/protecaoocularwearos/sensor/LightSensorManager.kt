package dev.hsantiago.protecaoocularwearos.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LightSensorManager(
    private val sensorManager: SensorManager
) {
    private val lightSensor: Sensor? =
        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    val isSensorAvailable: Boolean
        get() = lightSensor != null

    fun observeLightSensor(): Flow<Float> = callbackFlow {
        val sensor = lightSensor
        if (sensor == null) {
            Log.w(TAG, "Sensor TYPE_LIGHT not available on this device")
            close()
            return@callbackFlow
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.values?.firstOrNull()?.let { lux ->
                    trySend(lux)
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
            }
        }

        sensorManager.registerListener(
            listener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )

        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }

    private companion object {
        private const val TAG = "LightSensorManager"
    }
}
