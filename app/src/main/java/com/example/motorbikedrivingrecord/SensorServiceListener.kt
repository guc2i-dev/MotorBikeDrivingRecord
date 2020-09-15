package com.example.motorbikedrivingrecord

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.Sensor
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.util.Log

interface SensorServiceInterface : SensorServiceListener.Listener {
    fun onSensorChanged(sensorType : Int,values : FloatArray)
    fun onLocationChanged(values : Array<Double>)
}

class SensorServiceListener(private val context: Context) : Handler(){

    private lateinit var mService: SensorService
    private var listener: SensorServiceInterface? = null
    private val SENSOR = 1
    private val LOCATION = 2
    interface Listener {}
    var sensors: List<Sensor> = listOf()

    fun start(){
            val intent = Intent(context,SensorService::class.java)
            context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
            context.startService(intent)
    }
    fun stop(){
            val intent = Intent(context,SensorService::class.java)
            context.unbindService(mConnection)
            context.stopService(intent)
    }

    override fun handleMessage(msg: Message) {
        if (msg.arg1 == SENSOR){
            val values = msg.obj as FloatArray
            listener?.onSensorChanged(msg.arg2,values)
        }else if(msg.arg1 == LOCATION){
            val values = msg.obj as Array<Double>
            listener?.onLocationChanged(values)
        }
    }

    fun setListener(listener: Listener?) {
        if (listener is SensorServiceInterface) {
            this.listener = listener
        }
    }

    private fun setHandler(){
        mService.setHandler(this)
    }

    private val mConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val localBinder = binder as SensorService.LocalBinder
            mService = localBinder.getService()
            sensors =  mService.sensors
            setHandler()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }
}