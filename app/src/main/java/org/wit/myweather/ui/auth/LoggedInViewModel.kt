package org.wit.myweather.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.wit.myweather.firebase.FirebaseAuthManager
import org.wit.myweather.firebase.FirebaseDBManager
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.models.WeatherTemperatureModel

class LoggedInViewModel(app: Application) : AndroidViewModel(app) {

    var firebaseAuthManager : FirebaseAuthManager = FirebaseAuthManager(app,null)
    var liveFirebaseUser : MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser
    var loggedOut : MutableLiveData<Boolean> = firebaseAuthManager.loggedOut

    //For Preloading weather temperature cards
    private val weatherList = MutableLiveData<MutableList<WeatherModel>>()
    val observableWeather: LiveData<MutableList<WeatherModel>>
        get() = weatherList

    fun logOut() {
        firebaseAuthManager.logOut()
    }

    //For Preloading weather temperature cards
    fun getWeatherModels(){
        if(FirebaseAuth.getInstance().currentUser != null) {
            FirebaseDBManager.getAll(this.weatherList, FirebaseAuth.getInstance().currentUser)
        }
    }

    fun uploadWeatherTemperature(weatherTempModel: WeatherTemperatureModel){
        FirebaseDBManager.createWeatherTemperature(weatherTempModel,liveFirebaseUser)
    }

    fun updateWeatherTemperature(weatherTempModel: WeatherTemperatureModel){
        FirebaseDBManager.updateWeatherTemperature(weatherTempModel,liveFirebaseUser)
    }

    fun deleteWeatherTemperature(weatherTempModel: WeatherTemperatureModel){
        FirebaseDBManager.deleteWeatherTemperature(weatherTempModel,liveFirebaseUser)
    }
}