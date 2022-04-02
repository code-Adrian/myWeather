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
import org.wit.myweather.databinding.WeatherCardBinding
import org.wit.myweather.helpers.FireBase_Store
import org.wit.myweather.models.Json_Store
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
        val binding = WeatherCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       //return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.weather_card,parent,false))
        return MainHolder(binding)
    }

    override fun getItemCount(): Int = weather.size


    override fun onBindViewHolder(holder: WeatherAdapter.MainHolder, position: Int) {
        val weathers = weather[holder.adapterPosition]
        holder.bind(weathers,listener,listener2)
    }

    inner class MainHolder (val binding: WeatherCardBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(weathermodel: WeatherModel, listener: WeatherListener, listener2: EditListener){

            binding.weather = weathermodel
doAsync {
    uiThread {

    binding.root.setOnClickListener { listener.onWeatherClick(weathermodel) }
    binding.root.editImage.setOnClickListener { listener2.onEditClick(weathermodel) }
                      binding.imageIcon.setImageResource(weathermodel.Image)
                }
            }
        }
    }
}