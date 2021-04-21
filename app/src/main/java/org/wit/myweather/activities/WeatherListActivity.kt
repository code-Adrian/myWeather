package org.wit.myweather.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.mainmenu_activity.*
import kotlinx.android.synthetic.main.weather_card.*
import kotlinx.android.synthetic.main.weatherlist_activity.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.myweather.R
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel

class WeatherListActivity : AppCompatActivity(),WeatherListener,EditListener {
lateinit var app : Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weatherlist_activity)
        app = application as Main
        val layout = LinearLayoutManager(this)
        recyclerView.layoutManager = layout
loadWeather()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.weatherlist_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){

            R.id.weather_add -> {
                startActivityForResult<WeatherActivity>(0)
               // finish()
            }
        }

        when(item.itemId){
            R.id.weather_home -> {
                startActivityForResult<MenuActivity>(0)
                finish()}
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onWeatherClick(weather: WeatherModel) {
        startActivityForResult(intentFor<WeatherActivityTemperature>().putExtra("weather_temperature",weather),0)
    }

    override fun onEditClick(weather : WeatherModel) {
        startActivityForResult(intentFor<WeatherActivity>().putExtra("weather_edit",weather),0)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       super.onActivityResult(requestCode, resultCode, data)

       if(requestCode == 0){
           if(resultCode == RESULT_OK){
               loadWeather()
           }
       }


    }

 fun loadWeather(){
    showWeather(app.weather.getAll())
}

   fun showWeather(weather: MutableList<WeatherModel>){
       recyclerView.adapter = WeatherAdapter(weather, this,this)
       recyclerView.adapter?.notifyDataSetChanged()
    }
}