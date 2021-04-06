package org.wit.myweather.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.weather_card.view.*
import kotlinx.android.synthetic.main.weatherlist_activity.*
import org.wit.myweather.R
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.webscraper.getLowestTemp
import org.wit.myweather.webscraper.getPeakTemp
import org.wit.myweather.webscraper.setImage
import java.lang.Exception
import kotlin.concurrent.thread

interface WeatherListener{
    fun onWeatherClick(weather: WeatherModel)
}

class WeatherAdapter constructor(private val weather: MutableList<WeatherModel>, private val listener: WeatherListener) : RecyclerView.Adapter<WeatherAdapter.MainHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherAdapter.MainHolder {
       return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.weather_card,parent,false))
    }

    override fun getItemCount(): Int = weather.size


    override fun onBindViewHolder(holder: WeatherAdapter.MainHolder, position: Int) {
        val weathers = weather[holder.adapterPosition]
        holder.bind(weathers,listener)
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(weathermodel: WeatherModel, listener: WeatherListener){
                if (!weathermodel.County.equals("")) {
                    itemView.locationDescription.text = weathermodel.Country + ", " + weathermodel.County + ", " + weathermodel.City
                } else {
                    itemView.locationDescription.text = weathermodel.Country + ", " + weathermodel.City
                }

                weathermodel.Temperature = getPeakTemp(weathermodel.Country, weathermodel.County, weathermodel.City)
                weathermodel.TemperatureLow = getLowestTemp(weathermodel.Country, weathermodel.County, weathermodel.City)

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

                itemView.setOnClickListener { listener.onWeatherClick(weathermodel) }
                itemView.imageIcon.setImageResource(setImage(weathermodel.Country, weathermodel.County, weathermodel.City, weathermodel.WebLink))

        }
    }
}