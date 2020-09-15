package com.example.motorbikedrivingrecord

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocationSensorData(
    @PrimaryKey var datetime: String,
    var speed: String,
    var latitude: String,
    var longitude: String
)