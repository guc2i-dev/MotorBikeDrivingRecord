package com.example.motorbikedrivingrecord

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RotationSensorData::class], version = 1)
abstract class RotationSensorDataDatabase: RoomDatabase() {
    abstract fun rotationSensorDataDao(): RotationSensorDataDao

    companion object {

        @Volatile
        private var INSTANCE: RotationSensorDataDatabase? = null

        fun getDatabase(context: Context): RotationSensorDataDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RotationSensorDataDatabase::class.java,
                    "rotation_sensor_data_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}