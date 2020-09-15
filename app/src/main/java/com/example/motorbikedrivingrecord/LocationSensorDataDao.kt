package com.example.motorbikedrivingrecord

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface LocationSensorDataDao {

//    @Query("SELECT * FROM LocationSensorData WHERE datetime = :datetime")
//    fun loadAllSensorDataWithDatetime(datetime: Long): List<LocationSensorData>
//
//    @Query("SELECT * FROM LocationSensorData ORDER BY datetime")
//    fun getSerialSensorData(): List<LocationSensorData>

    @Insert
    fun insertLocationSensorData(locationSensorData : LocationSensorData)
}