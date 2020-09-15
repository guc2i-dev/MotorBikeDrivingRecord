package com.example.motorbikedrivingrecord

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [LocationSensorData::class], version = 1)
abstract class LocationSensorDataDatabase: RoomDatabase() {
    abstract fun locationSensorDataDao(): LocationSensorDataDao

    companion object {

        @Volatile
        private var INSTANCE: LocationSensorDataDatabase? = null

        fun getDatabase(context: Context): LocationSensorDataDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocationSensorDataDatabase::class.java,
                    "location_sensor_data_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}