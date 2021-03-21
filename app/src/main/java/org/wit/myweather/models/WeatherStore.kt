package org.wit.myweather.models

interface WeatherStore {
    fun getAll(): MutableList<WeatherModel>
    fun create(weather: WeatherModel)
    fun delete(weather: WeatherModel)
    fun update(weather: WeatherModel)
}