package com.example.motorbikedrivingrecord

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log

class SensorService : Service(), SensorEventListener {
//    private var handler : Handler? = null
    var sensors: List<Sensor> = listOf()

    override fun onCreate() {
        super.onCreate()
        val sensorManager: SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensors = sensorManager.getSensorList(Sensor.TYPE_ALL)
        if (sensors.isNotEmpty()) {
            for (i in sensors.indices) {
                val s: Sensor = sensors[i]
                sensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val sensorManager : SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        Log.d("","${event.values[0]}")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
