package org.wit.myweather.ui.weather
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.myweather.helpers.FireBase_Store
import org.wit.myweather.models.WeatherModel

class WeatherViewModel : ViewModel() {
    private val weatherList = MutableLiveData<MutableList<WeatherModel>>()

    val observableWeather: LiveData<MutableList<WeatherModel>>
        get() = weatherList

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    init {
        load()
    }

    fun load(){
        FireBase_Store.getAll(weatherList)
    }

    fun create(weather: WeatherModel){
        status.value = try {
            FireBase_Store.create(weather)
            true
        }catch (e: IllegalArgumentException){
            false
        }
    }
}