package com.example.motorbikedrivingrecord

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.*
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*

class SensorService : Service(), SensorEventListener {
    private var handler : Handler? = null
    private val SENSOR = 1
    private val LOCATION = 2
    var sensors: MutableList<Sensor> = mutableListOf<Sensor>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    companion object {
        lateinit var locationDB: LocationSensorDataDatabase
        lateinit var accDB: AccSensorDataDatabase
        lateinit var pressureDB: PressureSensorDataDatabase
        lateinit var rotationDB: RotationSensorDataDatabase
    }

    override fun onCreate() {
        super.onCreate()

        //センサーデータ取得準備
        val mSensorManager: SensorManager =
            getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val pressure: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
        val rotation: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

//        val magnetometer: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
//        val gyroscope: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
//        val geomagnetic: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR)

        sensors.add(accelerometer)
        sensors.add(pressure)
        sensors.add(rotation)
//        sensors.add(magnetometer)
//        sensors.add(gyroscope)
//        sensors.add(geomagnetic)

        for (i in sensors.indices) {
            val s: Sensor = sensors[i]
            mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL)
        }

        //すべてのセンサをチェックする
//        sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL)
//        if (sensors.isNotEmpty()) {
//            for (i in sensors.indices) {
//                val s: Sensor = sensors[i]
//                mSensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL)
//            }
//        }

        //データベースの初期化
        locationDB = LocationSensorDataDatabase.getDatabase(applicationContext)
        accDB = AccSensorDataDatabase.getDatabase(applicationContext)
        pressureDB = PressureSensorDataDatabase.getDatabase(applicationContext)
        rotationDB = RotationSensorDataDatabase.getDatabase(applicationContext)

        //ロケーションデータ取得準備
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    val msg = Message.obtain()
                    msg.arg1 = LOCATION
                    val locationArray = arrayOf(location.speed.toDouble(),location.latitude, location.longitude)
                    msg.obj = locationArray.clone()
                    if (handler != null) handler?.sendMessage(msg)

                    val datetime = System.currentTimeMillis()
                    var locationSensorData = LocationSensorData(datetime.toString(), location.speed.toString(), location.latitude.toString(), location.longitude.toString())
                    GlobalScope.async(Dispatchers.Default) {
                        val locationSensorDao = locationDB.locationSensorDataDao()
                        locationSensorDao.insertLocationSensorData(locationSensorData)
                    }
                }
            }
        }
        startLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        val sensorManager : SensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.unregisterListener(this)
        stopLocationUpdates()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        val msg = Message.obtain()
        msg.arg1 = SENSOR
        msg.arg2 = event.sensor.type
        msg.obj = event.values.clone()
        if (handler != null) handler?.sendMessage(msg)

        //データの保存
        val datetime = System.currentTimeMillis()
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                var accSensorData = AccSensorData(
                    datetime.toString(),
                    event.values[0].toString(),
                    event.values[1].toString(),
                    event.values[2].toString()
                )
                GlobalScope.async(Dispatchers.Default) {
                    val accSensorDao = accDB.accSensorDataDao()
                    accSensorDao.insertAccSensorData(accSensorData)
                }
            }
            Sensor.TYPE_PRESSURE -> {
                var pressureSensorData = PressureSensorData(
                    datetime.toString(),
                    event.values[0].toString()
                )
                GlobalScope.async(Dispatchers.Default) {
                    val pressureSensorDao = pressureDB.pressureSensorDataDao()
                    pressureSensorDao.insertAccSensorData(pressureSensorData)
                }
            }
            Sensor.TYPE_ROTATION_VECTOR -> {
                var rotationSensorData = RotationSensorData(
                    datetime.toString(),
                    event.values[0].toString(),
                    event.values[1].toString(),
                    event.values[2].toString(),
                    event.values[3].toString(),
                    event.values[4].toString()
                )
                GlobalScope.async(Dispatchers.Default) {
                    val rotationSensorDao = rotationDB.rotationSensorDataDao()
                    rotationSensorDao.insertAccSensorData(rotationSensorData)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun startLocationUpdates() {
        val locationRequest = createLocationRequest() ?: return
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null)
    }

    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun createLocationRequest(): LocationRequest? {
        return LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val binder = LocalBinder()

    inner class LocalBinder : Binder(), IBinder {
        fun getService(): SensorService = this@SensorService
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun setHandler(handler  : Handler){
        this.handler = handler
    }
}
