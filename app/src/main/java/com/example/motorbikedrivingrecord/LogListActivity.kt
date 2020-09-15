package com.example.motorbikedrivingrecord

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class LogListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loglist)

        val logDurationListView = findViewById<ListView>(R.id.logDurationList)

        val sharedPreferences = getSharedPreferences("LogDurationListStore", Context.MODE_PRIVATE)
        val gson = Gson()
        val listType: Type = object : TypeToken<List<LogDurationData>>() {}.type
        var logDurationList = ArrayList<LogDurationData>()
        if (sharedPreferences.contains("logDurationListStore")) {
            logDurationList =
                gson.fromJson(sharedPreferences.getString("logDurationListStore", null), listType)
        }
        val mLogListCustomAdapter: LogListCustomAdapter
        mLogListCustomAdapter = LogListCustomAdapter(this, logDurationList)
        logDurationListView.adapter = mLogListCustomAdapter

        logDurationListView.setOnItemClickListener{parent, view, position, id ->
            val itemTextView : TextView = view.findViewById(R.id.durationText)
            Toast.makeText(applicationContext, itemTextView.text.toString(), Toast.LENGTH_LONG).show()

            //グラフ画面へ遷移
            val intent = Intent(this.applicationContext, GraphActivity::class.java)
            val selectedData: LogDurationData? = mLogListCustomAdapter.getItem(position)
            intent.putExtra("starttime", selectedData?.startTime)
            intent.putExtra("endtime", selectedData?.endTime)
            startActivity(intent)
        }

        logDurationListView.setOnItemLongClickListener{parent, view, position, id ->
            val itemTextView : TextView = view.findViewById(R.id.durationText)
            Toast.makeText(applicationContext, "long["+position+"]\n"+itemTextView.text.toString(), Toast.LENGTH_LONG).show()

            //画面上の削除
            mLogListCustomAdapter.remove(mLogListCustomAdapter.getItem(position))
            mLogListCustomAdapter.notifyDataSetChanged()

            //sharedPreferenceへの反映
            val sharedPreferences = this.getSharedPreferences("LogDurationListStore",Context.MODE_PRIVATE)
            val gson = Gson()
            val listType: Type = object : TypeToken<List<LogDurationData>>() {}.type
            var logDurationList = ArrayList<LogDurationData>()
            logDurationList = gson.fromJson(sharedPreferences.getString("logDurationListStore", null), listType)
            logDurationList.removeAt(position)
            val shardPrefEditor = sharedPreferences.edit()
            shardPrefEditor.putString("logDurationListStore", gson.toJson(logDurationList))
            shardPrefEditor.apply()

            return@setOnItemLongClickListener true
        }
    }
}
