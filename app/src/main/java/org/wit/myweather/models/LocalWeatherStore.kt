package org.wit.myweather.models

interface LocalWeatherStore {
    fun getAll(): MutableList<WeatherModel>
    fun serialize(weather: MutableList<WeatherModel>)
    fun deserialize()
    fun update(weather: WeatherModel)
}