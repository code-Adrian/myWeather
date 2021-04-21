package org.wit.myweather.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.weatherlist_activity.*
import org.wit.myweather.R
import org.wit.myweather.main.Main

class WeatherActivityTemperature : AppCompatActivity(){

    lateinit var app : Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_activity_temperature)
        app = application as Main



    }
}