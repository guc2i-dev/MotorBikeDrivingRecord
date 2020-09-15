package com.example.motorbikedrivingrecord

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class GraphActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        val starttime = intent.getLongExtra("starttime",0)
        val endtime = intent.getLongExtra("endtime",0)

        Log.d("test","start:"+starttime+" end:"+endtime)
    }
}