package com.example.motorbikedrivingrecord

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class LogListCustomAdapter(context: Context, var mLogDurationDataList: List<LogDurationData>) : ArrayAdapter<LogDurationData>(context, 0, mLogDurationDataList) {

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val logDurationData = mLogDurationDataList[position]

        var view = convertView
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.list_item, parent, false)
        }

        val durationText = view?.findViewById<TextView>(R.id.durationText)
        durationText?.text = "starttime:【${logDurationData.startTime}】\nendtime:【${logDurationData.endTime}】"

        return view!!
    }
}