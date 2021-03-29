package org.wit.myweather.main

import android.app.Application
import org.wit.myweather.models.FireBase_Store
import org.wit.myweather.models.WeatherStore
import org.wit.myweather.models.*
class Main : Application() {

lateinit var weather: WeatherStore
    override fun onCreate() {
        super.onCreate()

        weather = FireBase_Store()
    }
}