package com.example.motorbikedrivingrecord

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AccSensorData(
    @PrimaryKey var datetime: String,
    var xValue: String,
    var yValue: String,
    var zValue: String
)