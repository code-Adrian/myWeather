package org.wit.myweather.ui.weathertemperature

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import org.wit.myweather.firebase.FirebaseDBManager
import org.wit.myweather.models.WeatherTemperatureModel

class WeatherTemperatureViewModel: ViewModel() {
    private val weatherTemp = MutableLiveData<MutableList<WeatherTemperatureModel>>()

    val observableWeather: LiveData<MutableList<WeatherTemperatureModel>>
        get() = weatherTemp


    fun getAllWeatherTempModel(){
        FirebaseDBManager.getAllWeatherTemperature(this.weatherTemp, FirebaseAuth.getInstance().currentUser)
    }

    fun getSpecifiedTempModel(modelid: String){
        FirebaseDBManager.getSpecificWeatherTemperature(modelid,this.weatherTemp,FirebaseAuth.getInstance().currentUser)
    }
}