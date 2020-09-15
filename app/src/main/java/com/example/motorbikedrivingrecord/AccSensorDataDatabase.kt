package com.example.motorbikedrivingrecord

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [AccSensorData::class], version = 1)
abstract class AccSensorDataDatabase: RoomDatabase() {
    abstract fun accSensorDataDao(): AccSensorDataDao

    companion object {

        @Volatile
        private var INSTANCE: AccSensorDataDatabase? = null

        fun getDatabase(context: Context): AccSensorDataDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AccSensorDataDatabase::class.java,
                    "acc_sensor_data_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}