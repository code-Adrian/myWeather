package org.wit.myweather.models

var Id = 0L

internal fun ID():Long{
    return Id++;
}

class FireBase_Store: WeatherStore {

    val weather = ArrayList<WeatherModel>()

    override fun getAll(): MutableList<WeatherModel> {
        return weather
    }

    override fun create(weather: WeatherModel) {
        TODO("Not yet implemented")
    }

    override fun delete(weather: WeatherModel) {
        TODO("Not yet implemented")
    }

    override fun update(weather: WeatherModel) {
        TODO("Not yet implemented")
    }


}