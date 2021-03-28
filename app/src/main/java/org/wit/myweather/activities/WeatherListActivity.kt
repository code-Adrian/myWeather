package org.wit.myweather.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.wit.myweather.R
import org.wit.myweather.main.Main

class WeatherListActivity : AppCompatActivity() {
lateinit var app : Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.weatherlist_activity)

    }
}