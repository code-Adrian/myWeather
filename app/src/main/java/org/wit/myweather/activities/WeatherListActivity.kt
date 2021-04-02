package org.wit.myweather.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.mainmenu_activity.*
import kotlinx.android.synthetic.main.weatherlist_activity.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.myweather.R
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel

class WeatherListActivity : AppCompatActivity(),WeatherListener {
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
            R.id.weather_add -> startActivityForResult<WeatherActivity>(0)
        }

        when(item.itemId){
            R.id.weather_home -> {finish()}
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onWeatherClick(weather: WeatherModel) {
        startActivityForResult(intentFor<WeatherActivity>().putExtra("weather_edit",weather),0)
finish()
    }

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        super.onActivityResult(requestCode, resultCode, data)
    }

private fun loadWeather(){
    showWeather(app.weather.getAll())
}

   fun showWeather(weather: MutableList<WeatherModel>){
        recyclerView.adapter = WeatherAdapter(weather,this)
       recyclerView.adapter?.notifyDataSetChanged()
    }

}