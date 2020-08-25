package com.example.motorbikedrivingrecord

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SensorViewModel  : ViewModel() {

    val currentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}
