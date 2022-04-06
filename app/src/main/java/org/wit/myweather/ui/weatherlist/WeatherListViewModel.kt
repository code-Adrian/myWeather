package org.wit.myweather.ui.weatherlist
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import org.wit.myweather.firebase.FirebaseDBManager
import org.wit.myweather.models.WeatherModel

class WeatherListViewModel: ViewModel() {

    private val weatherList = MutableLiveData<MutableList<WeatherModel>>()

    val observableWeatherList: LiveData<MutableList<WeatherModel>>
    get() = weatherList


    init {
        load()
    }
    fun load(){
        FirebaseDBManager.getAll(weatherList,FirebaseAuth.getInstance().currentUser)
    }

}