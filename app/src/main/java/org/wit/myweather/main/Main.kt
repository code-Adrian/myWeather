package org.wit.myweather.main

import android.app.Application
import org.wit.myweather.models.*

class Main : Application() {

//lateinit var weather: WeatherStore
lateinit var localWeather: Json_Store
    override fun onCreate() {
        super.onCreate()
        //weather = FireBase_Store()
        localWeather = Json_Store(applicationContext)
       // updater()

    }
    private fun updater(){
        //val updaterThread = thread(weather,localWeather)
       // updaterThread.start()
    }
}



