package org.wit.myweather.ui.weatherlist
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.wit.myweather.firebase.FirebaseDBManager
import org.wit.myweather.models.WeatherModel

class WeatherListViewModel: ViewModel() {

    private val weatherList = MutableLiveData<MutableList<WeatherModel>>()

    val observableWeatherList: LiveData<MutableList<WeatherModel>>
    get() = weatherList

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status

    init {
        load()
    }
    fun load(){
        FirebaseDBManager.getAll(weatherList,FirebaseAuth.getInstance().currentUser)
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