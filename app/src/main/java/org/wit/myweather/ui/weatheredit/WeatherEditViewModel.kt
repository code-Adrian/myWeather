package org.wit.myweather.ui.weatheredit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.myweather.helpers.FireBase_Store
import org.wit.myweather.models.WeatherModel

class WeatherEditViewModel : ViewModel() {
    private val weatherList = MutableLiveData<MutableList<WeatherModel>>()

    val observableWeatherEdit: LiveData<MutableList<WeatherModel>>
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
    fun update(weather: WeatherModel){
        status.value = try {
            FireBase_Store.update(weather)
            true
        }catch (e: IllegalArgumentException){
            false
        }
    }

    fun delete(weather: WeatherModel){
        status.value = try {
            FireBase_Store.delete(weather)
            true
        }catch (e: IllegalArgumentException){
            false
        }
    }
}