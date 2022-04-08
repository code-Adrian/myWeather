package org.wit.myweather.activities

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.weather_card.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.myweather.R
import org.wit.myweather.databinding.WeatherCardBinding
import org.wit.myweather.models.WeatherModel


interface WeatherListener{
    fun onWeatherClick(weather: WeatherModel)
}

interface FavListener{
    fun onFavClick(imageView: ImageView, weathermodel: WeatherModel)
}

class WeatherAdapter constructor(private var weather: MutableList<WeatherModel>, private val listener: WeatherListener, private val listener2: FavListener) : RecyclerView.Adapter<WeatherAdapter.MainHolder>(),Filterable{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherAdapter.MainHolder {
        val binding = WeatherCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
       //return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.weather_card,parent,false))
        return MainHolder(binding)
    }

    override fun getItemCount(): Int = weather.size

    internal var filterRestock: MutableList<WeatherModel>
    internal var filterListResult: MutableList<WeatherModel>
    init{
        filterListResult = weather
        filterRestock = weather
    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(charString: CharSequence?): FilterResults {
                val charSearch: String = charString.toString()
                if(charSearch.isEmpty()){
                    filterListResult = filterRestock
                }else{
                    val resultList = ArrayList<WeatherModel>()
                    for(row in weather)
                    {
                        if(row.City!!.lowercase().contains(charSearch.lowercase()))
                        {
                            resultList.add(row)
                        }
                    }
                    filterListResult = resultList

                }
                val filterResults: FilterResults = Filter.FilterResults()
                filterResults.values = filterListResult
                return filterResults
            }

            override fun publishResults(charSeq: CharSequence?, filterResults: FilterResults?) {
                weather = filterResults!!.values as MutableList<WeatherModel>
                notifyDataSetChanged()
            }

        }

    }



    fun removeAt(position: Int) {
        weather.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: WeatherAdapter.MainHolder, position: Int) {
        val weathers = weather[holder.adapterPosition]
        holder.bind(weathers,listener,listener2)
    }

    inner class MainHolder (val binding: WeatherCardBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(weathermodel: WeatherModel, listener: WeatherListener, listener2: FavListener){

                    binding.root.tag = weathermodel
                    binding.weather = weathermodel
doAsync {
    uiThread {
                    if(weathermodel.Favourite){
                        binding.root.favImage.setImageResource(R.drawable.favourite)
                    }else{
                        binding.root.favImage.setImageResource(R.drawable.notfavourite)
                    }
                    binding.root.setOnClickListener { listener.onWeatherClick(weathermodel) }
                    binding.root.favImage.setOnClickListener {  listener2.onFavClick(binding.root.favImage,weathermodel) }
                    binding.imageIcon.setImageResource(weathermodel.Image)
                }
            }
        }
    }

}