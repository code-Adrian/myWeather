package org.wit.myweather.fragments

import android.os.Bundle
import android.view.*
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
            app.weather.create(model.copy())
            thread {
                val action = WeatherFragmentDirections.actionWeatherFragmentToWeatherList()
                findNavController().navigate((action))
            }
        }
    }

    private fun addWeatherByText(){
        fragBinding.AddWeather.setOnClickListener {
            model.Country = fragBinding.AddCountry.text.toString()
            model.County = fragBinding.AddCounty.text.toString()
            model.City = fragBinding.AddCity.text.toString()

            if (fragBinding.AddCountry.text.isNotEmpty()) {
                if (fragBinding.AddCity.text.isNotEmpty()) {
                    app.weather.create(model.copy())

                } else {
                    Toast.makeText(context, "Please fill in the city field.", Toast.LENGTH_SHORT)
                }
            } else {
                Toast.makeText(context, "Please fill in the country field.", Toast.LENGTH_LONG)
            }
            thread {
                val action = WeatherFragmentDirections.actionWeatherFragmentToWeatherList()
                findNavController().navigate((action))
            }
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