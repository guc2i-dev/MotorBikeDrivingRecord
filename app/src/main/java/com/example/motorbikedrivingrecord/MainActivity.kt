package com.example.motorbikedrivingrecord

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.observe

class MainActivity : AppCompatActivity() {

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

        val sensorModel: SensorViewModel by viewModels()
        val sensorObserver = Observer<SensorData> {  sensorData ->
            timestampValue.text = sensorData.timestamp
        }
        sensorModel.sensorLiveData.observe(this, sensorObserver)



        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener{
            // ログの取得開始
            //val intent = Intent(this,SensorService::class.java)
            //this.startService(intent)

            //test用コード
            var tmp = SensorData()
            tmp.timestamp = "test"
            sensorModel.sensorLiveData.setValue(tmp)
        }

        val stopButton = findViewById<Button>(R.id.stopButton)
        stopButton.setOnClickListener {
            //ログの取得終了
            val intent = Intent(this,SensorService::class.java)
            this.stopService(intent)
        }

        val graphButton = findViewById<Button>(R.id.graphButton)
        graphButton.setOnClickListener{
            //ログの可視化
            val intent = Intent(applicationContext, GraphActivity::class.java)
            startActivity(intent)
        }
    }
}