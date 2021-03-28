package org.wit.myweather.activities

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.wit.myweather.models.WeatherModel

interface WeatherListener{
    fun onWeatherClick(weather: WeatherModel)
}

class WeatherAdapter constructor(private val weather: MutableList<WeatherModel>, private val listener: WeatherListener) : RecyclerView.Adapter<WeatherAdapter.MainHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherAdapter.MainHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: WeatherAdapter.MainHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bind(weather: WeatherModel, listener: WeatherListener){

        }

    }

}