package org.wit.myweather.models

interface Communicator {

    fun passDataString(string: String)
    fun passDataWeatherModel(list: WeatherModel)
}