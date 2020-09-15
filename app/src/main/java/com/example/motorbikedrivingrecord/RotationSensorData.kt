package com.example.motorbikedrivingrecord

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RotationSensorData(
    @PrimaryKey var datetime: String,
    var xValue: String,
    var yValue: String,
    var zValue: String,
    var cosValue: String,
    var acc: String
)