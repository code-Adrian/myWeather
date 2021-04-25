package org.wit.myweather.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.mainmenu_activity.*
import kotlinx.android.synthetic.main.weather_activity.*
import kotlinx.android.synthetic.main.weather_activity_edit.*
import kotlinx.android.synthetic.main.weatherlist_activity.*
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.myweather.R
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.webscraper.getLocationByWebLink

class WeatherActivity: AppCompatActivity() {

    var weather = WeatherModel()
    lateinit var app: Main
    var edit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as Main

        if (intent.hasExtra("weather_edit")) {
            setContentView(R.layout.weather_activity_edit)
            edit = true
            weather = intent.extras?.getParcelable<WeatherModel>("weather_edit")!!
            Edit_Country.setText(weather.Country)
            Edit_County.setText(weather.County)
            Edit_City.setText(weather.City)
            Edit_Link.setText(weather.WebLink)


        } else {
            setContentView(R.layout.weather_activity)

        }

        if (edit) {
            Edit_Weather.setOnClickListener {
                weather.Country = Edit_Country.text.toString()
                weather.County = Edit_County.text.toString()
                weather.City = Edit_City.text.toString()

                app.weather.update(weather.copy())
              //  startActivityForResult<WeatherListActivity>(0)
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
        } else  {
            Add_Weather.setOnClickListener {
                weather.Country = Add_Country.text.toString()
                weather.County = Add_County.text.toString()
                weather.City = Add_City.text.toString()

                if(Add_Country.text.isNotEmpty()){
                    if(Add_City.text.isNotEmpty()) {
                        app.weather.create(weather.copy())
                        setResult(AppCompatActivity.RESULT_OK)
                        finish()
                    }else{
                        toast("Please fill in the city field.")
                    }
                }else{
                    toast("Please fill in the country field.")
                }

              //  startActivityForResult<WeatherListActivity>(0)


            }

            Add_Weather_URL.setOnClickListener{

var location = getLocationByWebLink(Add_Link.text.toString())
             var list = location.split(",")
                weather.Country = list[1]
                weather.City = list[0]
                weather.WebLink = Add_Link.text.toString()
                app.weather.create(weather.copy())
              //  startActivityForResult<WeatherListActivity>(0)
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
        }

if(edit){
    toolbarEdit.setTitle("Edit Weather")
    setSupportActionBar(toolbarEdit)
}else{
    toolbarAdd.setTitle("Add Weather")
    setSupportActionBar(toolbarAdd)
}

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if(edit) {
            menuInflater.inflate(R.menu.weatheractivity_edit_menu, menu)
        }else{
            menuInflater.inflate(R.menu.weatheractivity_menu, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.edit_cancel -> {
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
        }

        when(item.itemId){
            R.id.edit_delete -> {
                app.weather.delete(weather)
                setResult(AppCompatActivity.RESULT_OK)
                startActivityForResult<WeatherListActivity>(0)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}