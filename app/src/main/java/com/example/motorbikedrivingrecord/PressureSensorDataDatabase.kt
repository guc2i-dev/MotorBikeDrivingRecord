package com.example.motorbikedrivingrecord

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [PressureSensorData::class], version = 1)
abstract class PressureSensorDataDatabase: RoomDatabase() {
    abstract fun pressureSensorDataDao(): PressureSensorDataDao

    companion object {

        @Volatile
        private var INSTANCE: PressureSensorDataDatabase? = null

        fun getDatabase(context: Context): PressureSensorDataDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PressureSensorDataDatabase::class.java,
                    "pressure_sensor_data_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}