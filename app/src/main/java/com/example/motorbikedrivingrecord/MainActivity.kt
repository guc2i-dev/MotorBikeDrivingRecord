package com.example.motorbikedrivingrecord

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


private const val PERMISSION_REQUEST_CODE = 1111 //location code

class MainActivity : AppCompatActivity() {
    val sensorServiceListener = SensorServiceListener(this)
    val TAG = "MBDR"

    private lateinit var speedValue: TextView
    private lateinit var latitudeValue: TextView
    private lateinit var longitudeValue: TextView
    private lateinit var xValue: TextView
    private lateinit var yValue: TextView
    private lateinit var zValue: TextView

    private var logExecFlg = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //センサーデータのラベル
        //accelerometer
        //magnetometer
        //pressure
        //gyroscope
        //rotation
        //geomagnetic
        speedValue = findViewById<TextView>(R.id.speed_value)
        latitudeValue = findViewById<TextView>(R.id.latitude_value)
        longitudeValue = findViewById<TextView>(R.id.longitude_value)
        xValue = findViewById<TextView>(R.id.x_value)
        yValue = findViewById<TextView>(R.id.y_value)
        zValue = findViewById<TextView>(R.id.z_value)

        var logDurationData = LogDurationData(0,0)
        var logDurationList = ArrayList<LogDurationData>()

        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener{
            if(!logExecFlg) {
                //ログの取得開始
                requestPermission()
                sensorServiceListener.setListener(sensorListener)
                sensorServiceListener.start()
                Toast.makeText(applicationContext, "センサーデータの収集を開始します。", Toast.LENGTH_LONG).show()

                //ログ開始時刻の記録
                logDurationData.startTime = System.currentTimeMillis()
                logExecFlg = true
            }
        }

        val stopButton = findViewById<Button>(R.id.stopButton)
        stopButton.setOnClickListener {
            if(logExecFlg) {
                //ログの取得終了
                sensorServiceListener.stop()
                Toast.makeText(applicationContext, "センサーデータの収集を終了します。", Toast.LENGTH_LONG).show()

                //ログ終了時刻の記録
                logDurationData.endTime = System.currentTimeMillis()

                //既存のLogDurationListへの追加
                val sharedPreferences = this.getSharedPreferences("LogDurationListStore",Context.MODE_PRIVATE)
                val gson = Gson()
                val listType: Type = object : TypeToken<List<LogDurationData>>() {}.type
                if(sharedPreferences.contains("logDurationListStore")) {
                    logDurationList = gson.fromJson(sharedPreferences.getString("logDurationListStore", null), listType)
                }
                logDurationList.add(logDurationData)

                //sharedPreferenceへの格納
                val shardPrefEditor = sharedPreferences.edit()
                shardPrefEditor.putString("logDurationListStore", gson.toJson(logDurationList))
                shardPrefEditor.apply()

                //初期化
                logDurationList.clear()
                logExecFlg = false
            }
        }

        val graphButton = findViewById<Button>(R.id.graphButton)
        graphButton.setOnClickListener{
            //ログの可視化
            val intent = Intent(applicationContext, LogListActivity::class.java)
            startActivity(intent)
        }
    }

    private fun requestPermission() {
        val permissionAccessCoarseLocationApproved =
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED

        if (permissionAccessCoarseLocationApproved) {
            val backgroundLocationPermissionApproved = ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED
            if (!backgroundLocationPermissionApproved) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION), PERMISSION_REQUEST_CODE)
            }
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ), PERMISSION_REQUEST_CODE
            )
        }
    }

    private val sensorListener = object : SensorServiceInterface{
        override fun onSensorChanged(sensorType: Int, values: FloatArray) {
            when (sensorType){
                Sensor.TYPE_ACCELEROMETER -> {
                    xValue.text = values[0].toString()
                    yValue.text = values[1].toString()
                    zValue.text = values[2].toString()
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                }
                Sensor.TYPE_PRESSURE -> {
                }
                Sensor.TYPE_GYROSCOPE -> {
                }
                Sensor.TYPE_ROTATION_VECTOR -> {
                }
                Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR -> {
                }
            }

        }

        override fun onLocationChanged(values: Array<Double>) {
            speedValue.text = values[0].toString()
            latitudeValue.text = values[1].toString()
            longitudeValue.text = values[2].toString()
        }
    }
}