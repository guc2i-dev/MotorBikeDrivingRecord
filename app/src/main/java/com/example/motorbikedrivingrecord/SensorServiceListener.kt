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

class SensorServiceListener : Service(), SensorEventListener {
    private var handler : Handler? = null
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
        val msg = Message.obtain()               // センサー値取得イベントを発生させる
        msg.arg1 = 1                                        // センサー値取得イベントを示す値
        msg.arg2 = event.sensor.type                        // センサーの種類を渡す
        msg.obj = event.values.clone()                      // センサーの値をコピーして渡す
        if (handler != null) handler?.sendMessage(msg)      // メッセージ送信
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        val msg = Message.obtain()               // レンジ変更イベントを発生させる
        msg.arg1 = 2                                        // レンジ変更イベントを示す値
        msg.arg2 = sensor!!.type.toInt()                      // センサーの種別
        msg.obj = accuracy
        if (handler != null) handler?.sendMessage(msg)      // メッセージ送信
    }

    private val binder = LocalBinder()

    inner class LocalBinder : Binder(), IBinder {
        fun getService(): SensorServiceListener = this@SensorServiceListener
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun setHandler(handler  : Handler){
        this.handler = handler
    }
}
