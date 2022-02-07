package org.wit.myweather.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import org.jetbrains.anko.toast
import org.wit.myweather.R
import org.wit.myweather.databinding.FragmentWeatherBinding
import org.wit.myweather.databinding.FragmentWeatherEditBinding
import org.wit.myweather.databinding.FragmentWeatherListBinding
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.webscraper.getLocationByWebLink
import org.wit.myweather.webscraper.getLowestTemp
import org.wit.myweather.webscraper.getPeakTemp
import org.wit.myweather.webscraper.setImage
import kotlin.concurrent.thread


class WeatherFragment : Fragment() {

    lateinit var app: Main
    private var _fragBinding: FragmentWeatherBinding? = null
    private val fragBinding get() = _fragBinding!!
    var model = WeatherModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as Main
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentWeatherBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        activity?.title = getString(R.string.action_menu)

        activateListeners()



        return root
    }


    private fun activateListeners(){
        addWeatherByText()
        addWeatherByURL()
    }

    private fun addWeatherByURL(){
        fragBinding.AddWeatherURL.setOnClickListener {
            var location = getLocationByWebLink(fragBinding.AddLink.text.toString())
            var list = location.split(",")
            model.Country = list[1]
            model.City = list[0]
            model.WebLink = fragBinding.AddLink.text.toString()

            model.Image = setImage(model.Country, model.County, model.City, model.WebLink)
            //Scrapes relevant info and sets respective Peak temperature.
            model.Temperature = getPeakTemp(model.Country, model.County, model.City)
            //Scrapes relevant info and sets respective Low temperature.
            model.TemperatureLow = getLowestTemp(model.Country, model.County, model.City)
            //Uploads Model to Firebase
            app.weather.create(model.copy())
            //Receives info from Firebase and saves locally

            app.weather.create(model.copy())
            app.localWeather.serialize(app.weather.getAll())
                val action = WeatherFragmentDirections.actionWeatherFragmentToWeatherList()
                findNavController().navigate((action))

        }
    }

    private fun addWeatherByText(){
        fragBinding.AddWeather.setOnClickListener {
            model.Country = fragBinding.AddCountry.text.toString()
            model.County = fragBinding.AddCounty.text.toString()
            model.City = fragBinding.AddCity.text.toString()

            if (fragBinding.AddCountry.text.isNotEmpty()) {
                if (fragBinding.AddCity.text.isNotEmpty()) {
                    //Scrapes relevant info and sets respective image.
                    model.Image = setImage(model.Country, model.County, model.City, model.WebLink)
                    //Scrapes relevant info and sets respective Peak temperature.
                    model.Temperature = getPeakTemp(model.Country, model.County, model.City)
                    //Scrapes relevant info and sets respective Low temperature.
                    model.TemperatureLow = getLowestTemp(model.Country, model.County, model.City)
                    //Uploads Model to Firebase
                    app.weather.create(model.copy())
                    //Receives info from Firebase and saves locally
                    app.localWeather.serialize(app.weather.getAll())


                    val key = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    key.hideSoftInputFromWindow(view?.getWindowToken(),0)
                } else {
                    Toast.makeText(context, "Please fill in the city field.", Toast.LENGTH_SHORT)
                }
            } else {
                Toast.makeText(context, "Please fill in the country field.", Toast.LENGTH_LONG)
            }

                val action = WeatherFragmentDirections.actionWeatherFragmentToWeatherList()
                findNavController().navigate((action))

        }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.weather_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)

    }






    companion object {
        @JvmStatic
        fun newInstance() =
            WeatherFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}