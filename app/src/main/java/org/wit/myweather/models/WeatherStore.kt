package org.wit.myweather.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface WeatherStore {
    fun getAll (weather:MutableLiveData<MutableList<WeatherModel>>, firebaseUser: FirebaseUser?)
    fun getAllFavourite (weather:MutableLiveData<MutableList<WeatherModel>>, firebaseUser: FirebaseUser?)
    fun localgetAll(): MutableList<WeatherModel>
    fun create(weather: WeatherModel,firebaseUser: MutableLiveData<FirebaseUser>)
    fun delete(weather: WeatherModel,firebaseUser: MutableLiveData<FirebaseUser>)
    fun update(weather: WeatherModel,firebaseUser: MutableLiveData<FirebaseUser>)
}