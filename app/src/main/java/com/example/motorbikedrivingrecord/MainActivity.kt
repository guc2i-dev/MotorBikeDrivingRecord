package com.example.motorbikedrivingrecord

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    val sensorService = SensorService(this)
    val TAG = "MBDR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //センサーデータのラベル
        val timestampValue = findViewById<TextView>(R.id.timestamp_value)
        val speedValue = findViewById<TextView>(R.id.speed_value)
        val latitudeValue = findViewById<TextView>(R.id.latitude_value)
        val longitudeValue = findViewById<TextView>(R.id.longitude_value)
        val xValue = findViewById<TextView>(R.id.x_value)
        val yValue = findViewById<TextView>(R.id.y_value)
        val zValue = findViewById<TextView>(R.id.z_value)
        val azimuthValue = findViewById<TextView>(R.id.azimuth_value)
        val pitchValue = findViewById<TextView>(R.id.pitch_value)
        val rollValue = findViewById<TextView>(R.id.roll_value)

        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener{
            //ログの取得開始
            sensorService.setListener(sensorListener)
            sensorService.start()
        }

        val stopButton = findViewById<Button>(R.id.stopButton)
        stopButton.setOnClickListener {
            //ログの取得終了
            sensorService.stop()
        }

        val graphButton = findViewById<Button>(R.id.graphButton)
        graphButton.setOnClickListener{
            //ログの可視化
            val intent = Intent(applicationContext, GraphActivity::class.java)
            startActivity(intent)
        }
    }

    private val sensorListener = object : SensorServiceInterface{
        var set: MutableSet<String> = mutableSetOf()

        override fun onSensorChanged(sensorType: Int, values: FloatArray) {

            var idx = -1
            for (i in sensorService.sensors.indices){
                if(sensorService.sensors[i].type == sensorType){
                    idx = i
                }
            }
            if (idx != -1) {
                Log.d(TAG,"$sensorType ${sensorService.sensors[idx].name} ${values[0]}")
                set.add(sensorService.sensors[idx].name)
            }
            Log.d(TAG,set.toString())
        }
        override fun onAccuracyChanged(sensorType: Int, accuracy: Int) {}
    }
}