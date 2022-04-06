package org.wit.myweather.main

import android.app.Application
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import org.wit.myweather.R
import org.wit.myweather.databinding.HomeBinding
import org.wit.myweather.models.*

class Main : Application() {


lateinit var localWeather: Json_Store
    override fun onCreate() {
        super.onCreate()

        localWeather = Json_Store(applicationContext)

    }

}



