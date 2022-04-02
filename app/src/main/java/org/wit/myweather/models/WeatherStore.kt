package org.wit.myweather.models

import androidx.lifecycle.MutableLiveData

interface WeatherStore {
    fun getAll (weather:MutableLiveData<MutableList<WeatherModel>>)
    fun localgetAll(): MutableList<WeatherModel>
    fun create(weather: WeatherModel)
    fun delete(weather: WeatherModel)
    fun update(weather: WeatherModel)
}