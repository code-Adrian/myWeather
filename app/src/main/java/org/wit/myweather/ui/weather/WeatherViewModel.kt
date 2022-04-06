package org.wit.myweather.ui.weather
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.wit.myweather.firebase.FirebaseAuthManager
import org.wit.myweather.firebase.FirebaseDBManager
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
        FirebaseDBManager.getAll(weatherList, FirebaseAuth.getInstance().currentUser)
    }

    fun create(weather: WeatherModel,liveFirebaseUser: MutableLiveData<FirebaseUser>){
        status.value = try {
            FirebaseDBManager.create(weather,liveFirebaseUser)
            true
        }catch (e: IllegalArgumentException){
            false
        }
    }
}