package org.wit.myweather.main

import android.app.Application
import android.widget.TextView
import org.wit.myweather.API.getLow
import org.wit.myweather.API.getPeak
import org.wit.myweather.API.setIcon
import org.wit.myweather.R
import org.wit.myweather.helpers.FireBase_Store
import org.wit.myweather.models.WeatherStore
import org.wit.myweather.models.*
import org.wit.myweather.webscraper.getLowestTemp
import org.wit.myweather.webscraper.getPeakTemp
import org.wit.myweather.webscraper.setImage
import java.lang.Exception

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
    //    while(infinite.equals(infinite)){
            var list = mutableListOf<WeatherModel>()
            var model = WeatherModel()
            //Wait 5 seconds before loading
            Thread.sleep(5000)


            if(weather2.localgetAll().size > 0){
                list = weather2.localgetAll()
            }
            println(list.size)
            try {
                if (list.size > 0) {
                    for (i in list) {
                        if(i.Type.equals("Scrape")) {
                            i.Temperature = getPeakTemp(i.Country, i.County, i.City)
                            i.TemperatureLow = getLowestTemp(i.Country, i.County, i.City)
                            i.Image = setImage(i.Country, i.County, i.City, i.WebLink)
                            i.id = i.id
                            i.Country = i.Country
                            i.City = i.City
                            i.WebLink = i.WebLink

                            model = WeatherModel(
                                i.id,
                                i.Country,
                                i.County,
                                i.City,
                                i.Temperature,
                                i.TemperatureLow,
                                i.WebLink,
                                i.Image
                            )
                            //Sleep for 1.5 seconds to avoid Concurrent Modification Exception
                            Thread.sleep(1500)
                            weather2.update(model.copy())
                        }else{
                            i.Temperature = getPeak(i.Country, i.County, i.City)
                            i.TemperatureLow = getLow(i.Country, i.County, i.City)
                            i.Image = setIcon(i.Country, i.County, i.City).get(0)
                            i.id = i.id
                            i.Country = i.Country
                            i.City = i.City
                            i.WebLink = i.WebLink

                            model = WeatherModel(
                                i.id,
                                i.Country,
                                i.County,
                                i.City,
                                i.Temperature,
                                i.TemperatureLow,
                                i.WebLink,
                                i.Image
                            )
                            //Sleep for 1.5 seconds to avoid Concurrent Modification Exception
                            Thread.sleep(1500)
                            weather2.update(model.copy())
                        }
                    }
                    localWeather2.serialize(weather2.getAll())
                }
            }catch (e: Exception){
                print("===================== Concurrent Modification Exception =====================")
            }
            //Thread.sleep(60000)
     //   }
    }
}