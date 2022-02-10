package org.wit.myweather.activities

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.weather_card.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.myweather.R
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.webscraper.getLowestTemp
import org.wit.myweather.webscraper.getPeakTemp
import org.wit.myweather.webscraper.setImage

interface WeatherListener{
    fun onWeatherClick(weather: WeatherModel)
}

interface EditListener{
    fun onEditClick(weather : WeatherModel)
}

class WeatherAdapter constructor(private val weather: MutableList<WeatherModel>, private val listener: WeatherListener, private val listener2: EditListener) : RecyclerView.Adapter<WeatherAdapter.MainHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherAdapter.MainHolder {
       return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.weather_card,parent,false))
    }

    override fun getItemCount(): Int = weather.size


    override fun onBindViewHolder(holder: WeatherAdapter.MainHolder, position: Int) {
        val weathers = weather[holder.adapterPosition]
        holder.bind(weathers,listener,listener2)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(weathermodel: WeatherModel, listener: WeatherListener, listener2: EditListener){
                if (!weathermodel.County.equals("")) {
                    itemView.locationDescription.text = weathermodel.Country + ", " + weathermodel.County + ", " + weathermodel.City
                } else {
                    itemView.locationDescription.text = weathermodel.Country + ", " + weathermodel.City
                }
doAsync { uiThread {

    itemView.setOnClickListener { listener.onWeatherClick(weathermodel) }
    itemView.editImage.setOnClickListener { listener2.onEditClick(weathermodel) }

               // weathermodel.Temperature = getPeakTemp(weathermodel.Country, weathermodel.County, weathermodel.City)
                //weathermodel.TemperatureLow = getLowestTemp(weathermodel.Country, weathermodel.County, weathermodel.City)
                  if(weathermodel.Type.equals( "Scrape")) {
                      weathermodel.Temperature = weathermodel.Temperature
                      weathermodel.TemperatureLow = weathermodel.TemperatureLow
                      if (weathermodel.TemperatureLow.equals("1000")) {
                          itemView.temperaturelowDetail.text = "null"
                      } else {
                          itemView.temperaturelowDetail.text = weathermodel.TemperatureLow + "°C"
                      }

                      if (weathermodel.Temperature.equals("1000")) {
                          itemView.temperatureDetail.text = "null"
                      } else {
                          itemView.temperatureDetail.text = weathermodel.Temperature + "°C"
                      }


                      //itemView.imageIcon.setImageResource(setImage(weathermodel.Country, weathermodel.County, weathermodel.City, weathermodel.WebLink))
                      itemView.imageIcon.setImageResource(weathermodel.Image)
                  }

                  if(weathermodel.Type.equals("API")){
                      weathermodel.Temperature = weathermodel.Temperature
                      weathermodel.TemperatureLow = weathermodel.TemperatureLow
                      itemView.temperaturelowDetail.text = weathermodel.TemperatureLow + "°C"
                      itemView.temperatureDetail.text = weathermodel.Temperature + "°C"

                      itemView.imageIcon.setImageResource(weathermodel.Image)
                  }
} }
        }
    }
}