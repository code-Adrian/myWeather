package org.wit.myweather.main

import android.app.Application
import android.widget.TextView
import org.wit.myweather.API.getLow
import org.wit.myweather.API.getPeak
import org.wit.myweather.API.setIcon
import org.wit.myweather.R
import org.wit.myweather.helpers.FireBase_Store
import org.wit.myweather.models.WeatherStore
import org.wit.myweather.models.*
import org.wit.myweather.webscraper.getLowestTemp
import org.wit.myweather.webscraper.getPeakTemp
import org.wit.myweather.webscraper.setImage
import java.lang.Exception

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



