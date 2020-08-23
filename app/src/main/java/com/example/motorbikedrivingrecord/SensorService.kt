package com.example.motorbikedrivingrecord

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Message

interface SensorServiceInterface : SensorService.Listener {         // 独自のイベントリスナー
    fun onSensorChanged(sensorType : Int,values : FloatArray)       // センサー値取得イベント
    fun onAccuracyChanged(sensorType : Int,accuracy : Int)          // センサーレンジ変更イベント
}

class SensorService(private val context: Context) : Handler(){      // 様々なセンサーの情報を管理するクラス

    private lateinit var mService: SensorServiceListener            // センサー取得サービスの参照用
    private var listener: SensorServiceInterface? = null            // 外部へのリスナー仲介
    interface Listener {}

    var sensors: List<Sensor> = listOf()                            // センサー情報

    fun start(){
        val intent = Intent(context,SensorServiceListener::class.java)
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
        context.startService(intent)
    }
    fun stop(){
        val intent = Intent(context,SensorServiceListener::class.java)
        context.unbindService(mConnection)
        context.stopService(intent)
    }

    override fun handleMessage(msg: Message) {                  // スレッド間通信用メッセージクラス

        if (msg.arg1 == 1){                                     // センサーの値取得時
            val values = msg.obj as FloatArray      //センサーの値を参照
            listener?.onSensorChanged(msg.arg2,values)          // センサー値取得イベント
        }
        if (msg.arg1 == 2){                                                     // センサーのレンジ変更時
            listener?.onAccuracyChanged(msg.arg2,msg.obj.toString().toInt())    // レンジ変更イベント
        }
    }

    fun setListener(listener: Listener?) {         // イベント受け取り先を設定
        if (listener is SensorServiceInterface) {
            this.listener = listener
        }
    }

    private fun setHandler(){
        mService.setHandler(this)
    }

    private val mConnection = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val localBinder = binder as SensorServiceListener.LocalBinder
            mService = localBinder.getService()
            sensors =  mService.sensors
            setHandler()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
        }
    }
}