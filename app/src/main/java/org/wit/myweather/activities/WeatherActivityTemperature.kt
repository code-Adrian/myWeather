package org.wit.myweather.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.mainmenu_activity.*
import kotlinx.android.synthetic.main.weather_activity_edit.*
import kotlinx.android.synthetic.main.weather_activity_temperature.*
import kotlinx.android.synthetic.main.weatherlist_activity.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.myweather.R
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.webscraper.*

class WeatherActivityTemperature : AppCompatActivity(){
    var weather = WeatherModel()
    lateinit var app : Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.weather_activity_temperature)
        app = application as Main
        if (intent.hasExtra("weather_temperature")) {

            weather = intent.extras?.getParcelable<WeatherModel>("weather_temperature")!!
            toolbarTemperature.setTitle("Week Temperature")
setSupportActionBar(toolbarTemperature)
setDetails()
        }

    }


    private fun setDetails(){

            doAsync {
                uiThread {

                    var peakTemplist = ArrayList<String>()
                    var lowTemplist = ArrayList<String>()
                    var weekDaylist = ArrayList<String>()
                    var imagelist = ArrayList<Int>()
                    peakTemplist = getWeeklyPeakTemp(weather.Country, weather.County, weather.City)
                    lowTemplist = getWeeklylowTemp(weather.Country, weather.County, weather.City)
                    weekDaylist = getWeekDays(weather.Country, weather.County, weather.City)
                    imagelist = getWeeklyWeather(weather.Country, weather.County, weather.City)


                    card1_peakTemp.text = peakTemplist.get(0)
                    card2_peakTemp.text = peakTemplist.get(1)
                    card3_peakTemp.text = peakTemplist.get(2)
                    card4_peakTemp.text = peakTemplist.get(3)
                    card5_peakTemp.text = peakTemplist.get(4)
                    card6_peakTemp.text = peakTemplist.get(5)
                    card7_peakTemp.text = peakTemplist.get(6)
                    card8_peakTemp.text = peakTemplist.get(7)

                    card1_lowTemp.text = lowTemplist.get(0)
                    card2_lowTemp.text = lowTemplist.get(1)
                    card3_lowTemp.text = lowTemplist.get(2)
                    card4_lowTemp.text = lowTemplist.get(3)
                    card5_lowTemp.text = lowTemplist.get(4)
                    card6_lowTemp.text = lowTemplist.get(5)
                    card7_lowTemp.text = lowTemplist.get(6)
                    card8_lowTemp.text = lowTemplist.get(7)


                    card2_Day.text = weekDaylist.get(1)
                    card3_Day.text = weekDaylist.get(2)
                    card4_Day.text = weekDaylist.get(3)
                    card5_Day.text = weekDaylist.get(4)
                    card6_Day.text = weekDaylist.get(5)
                    card7_Day.text = weekDaylist.get(6)
                    card8_Day.text = weekDaylist.get(7)

                    card1_image.setImageResource(setImage(weather.Country, weather.County, weather.City, weather.WebLink))
                    card2_image.setImageResource(imagelist.get(1))
                    card3_image.setImageResource(imagelist.get(2))
                    card4_image.setImageResource(imagelist.get(3))
                    card5_image.setImageResource(imagelist.get(4))
                    card6_image.setImageResource(imagelist.get(5))
                    card7_image.setImageResource(imagelist.get(6))
                    card8_image.setImageResource(imagelist.get(7))
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.weather_temperature,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            //not true
            R.id.Add_City -> {
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
        }


        return super.onOptionsItemSelected(item)
    }
}