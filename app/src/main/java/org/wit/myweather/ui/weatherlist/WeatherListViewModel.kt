package org.wit.myweather.ui.weatherlist
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.myweather.helpers.FireBase_Store
import org.wit.myweather.main.Main
import org.wit.myweather.models.Json_Store
import org.wit.myweather.models.LocalWeatherStore
import org.wit.myweather.models.WeatherModel

class WeatherListViewModel: ViewModel() {

    private val weatherList = MutableLiveData<MutableList<WeatherModel>>()

    val observableWeatherList: LiveData<MutableList<WeatherModel>>
    get() = weatherList

    init {
        load()
    }
    fun load(){
            FireBase_Store.getAll(weatherList)
    }

}