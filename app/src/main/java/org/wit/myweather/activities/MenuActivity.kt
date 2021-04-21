package org.wit.myweather.activities

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.os.StrictMode
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.mainmenu_activity.*
import kotlinx.android.synthetic.main.weather_card.view.*
import kotlinx.android.synthetic.main.weatherlist_activity.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.uiThread
import org.wit.myweather.R
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.webscraper.*
import kotlin.concurrent.thread

class MenuActivity : AppCompatActivity(){
    lateinit var app : Main
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        app = application as Main
        setSupportActionBar(toolbar)
        setContentView(R.layout.mainmenu_activity)
        loadDetails()
onMenuButtonClick()
        preloadWeather()
        progressBar.visibility = View.INVISIBLE
    }

    fun onMenuButtonClick(){
        menu_button.setOnClickListener {
            startActivityForResult(intentFor<WeatherListActivity>(),0)
progressBar.visibility = View.VISIBLE

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
loadDetails()
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun preloadWeather(){
       app.weather.getAll()
    }

private fun loadDetails(){
    doAsync {
        uiThread {
            var policy = StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            menu_county.text=getLocation()
            menu_dayofweek.text = getDateDay()
            menu_temp.text = getPeakTemp()
            weatherIcon.setBackgroundResource(getWeatherStatus())
        }
    }
}

}