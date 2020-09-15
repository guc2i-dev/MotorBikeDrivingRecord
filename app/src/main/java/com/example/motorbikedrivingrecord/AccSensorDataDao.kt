package com.example.motorbikedrivingrecord

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.util.*

@Dao
interface AccSensorDataDao {

//    @Query("SELECT * FROM AccSensorData WHERE datetime = :datetime")
//    fun loadAllSensorDataWithDatetime(datetime: Long): List<AccSensorData>
//
//    @Query("SELECT * FROM AccSensorData ORDER BY datetime")
//    fun getSerialSensorData(): List<AccSensorData>

    @Insert
    fun insertAccSensorData(accSensorData : AccSensorData)
}