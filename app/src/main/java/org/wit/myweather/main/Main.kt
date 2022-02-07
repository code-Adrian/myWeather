package org.wit.myweather.main

import android.app.Application
import android.widget.TextView
import org.wit.myweather.R
import org.wit.myweather.models.FireBase_Store
import org.wit.myweather.models.WeatherStore
import org.wit.myweather.models.*
import org.wit.myweather.webscraper.getLowestTemp
import org.wit.myweather.webscraper.getPeakTemp
import org.wit.myweather.webscraper.setImage

class Main : Application() {

lateinit var weather: WeatherStore
lateinit var localWeather: Json_Store
    override fun onCreate() {
        super.onCreate()
        weather = FireBase_Store()
        localWeather = Json_Store(applicationContext)
        updater()

    }
    private fun updater(){
        val updaterThread = thread(weather,localWeather)
        updaterThread.start()
    }
}

class  thread():Thread(){
    lateinit var weather2: WeatherStore
    lateinit var localWeather2: Json_Store

    constructor(weather: WeatherStore,localWeather: Json_Store):this(){
        this.weather2 = weather
        this.localWeather2 = localWeather
    }
    override fun run() {
        val infinite : String = "infiniteloop"
        while(infinite.equals(infinite)){
            var list = mutableListOf<WeatherModel>()
            var model = WeatherModel()
            //Wait 4 seconds before loading
            Thread.sleep(4000)
            list = weather2.getAll()
            Thread.sleep(8000)

            for (i in list){
                 i.Temperature = getPeakTemp(i.Country, i.County, i.City)
                 i.TemperatureLow = getLowestTemp(i.Country, i.County,i.City)
                 i.Image = setImage(i.Country, i.County, i.City, i.WebLink)
                 i.id = i.id
                 i.Country = i.Country
                 i.City = i.City
                 i.WebLink = i.WebLink

                model = WeatherModel(i.id,i.Country,i.County,i.City,i.Temperature,i.TemperatureLow,i.WebLink,i.Image)
                weather2.update(model.copy())

            }
              localWeather2.serialize(weather2.getAll())
            //Wait 2 minutes before updating again
            Thread.sleep(120000)
        }
    }
}