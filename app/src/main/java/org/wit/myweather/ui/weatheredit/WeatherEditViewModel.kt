package org.wit.myweather.ui.weatheredit
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.wit.myweather.firebase.FirebaseAuthManager
import org.wit.myweather.firebase.FirebaseDBManager
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
        FirebaseDBManager.getAll(weatherList, FirebaseAuth.getInstance().currentUser)
    }
    fun update(weather: WeatherModel,firebaseUser: MutableLiveData<FirebaseUser>){
        status.value = try {
            FirebaseDBManager.update(weather,firebaseUser)
            true
        }catch (e: IllegalArgumentException){
            false
        }
    }

    fun delete(weather: WeatherModel,firebaseUser: MutableLiveData<FirebaseUser>){
        status.value = try {
            FirebaseDBManager.delete(weather,firebaseUser)
            true
        }catch (e: IllegalArgumentException){
            false
        }
    }
}