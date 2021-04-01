package org.wit.myweather.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.weather_activity.*
import kotlinx.android.synthetic.main.weather_activity_edit.*
import org.jetbrains.anko.startActivityForResult
import org.wit.myweather.R
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel

class WeatherActivity: AppCompatActivity() {

    var weather = WeatherModel()
    lateinit var app : Main
   var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as Main

        if(intent.hasExtra("weather_edit")){
            setContentView(R.layout.weather_activity_edit)
         edit = true
            weather = intent.extras?.getParcelable<WeatherModel>("weather_edit")!!
            Edit_Country.setText(weather.Country)
            Edit_County.setText(weather.County)
            Edit_City.setText(weather.City)
            Edit_Link.setText(weather.WebLink)


        }else{
            setContentView(R.layout.weather_activity)
        }

Add_Weather.setOnClickListener {
    if(edit){
        app.weather.update(weather.copy())
    }else{
        app.weather.create(weather.copy())
    }

    setResult(AppCompatActivity.RESULT_OK)
    finish()
}

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.weatheractivity_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit_cancel -> {finish()}
        }


        return super.onOptionsItemSelected(item)
    }
}