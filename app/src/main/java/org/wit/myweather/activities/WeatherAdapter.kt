package org.wit.myweather.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.weather_card.view.*
import org.wit.myweather.R
import org.wit.myweather.models.WeatherModel

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

itemView.locationDescription.text = weathermodel.Country+", "+weathermodel.County+", "+weathermodel.City
            itemView.temperatureDetail.text = weathermodel.Temperature
            itemView.setOnClickListener{listener.onWeatherClick(weathermodel)}

            val temp : Int = weathermodel.Temperature.toInt()

            if(temp >= 14){
                itemView.imageIcon.setImageResource(R.drawable.sun)
            }else if(temp < 14){
                itemView.imageIcon.setImageResource(R.drawable.cloudysunny)
            }
            if(temp <= 8){
                itemView.imageIcon.setImageResource(R.drawable.verycloudy)
            }else if(temp <= 5){
                itemView.imageIcon.setImageResource(R.drawable.rain)
            }
        }

    }

}