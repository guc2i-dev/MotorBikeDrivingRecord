package com.example.motorbikedrivingrecord

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PressureSensorData(
    @PrimaryKey var datetime: String,
    var hPaValue: String
)