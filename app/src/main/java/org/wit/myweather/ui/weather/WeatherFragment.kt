package org.wit.myweather.ui.weather

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import org.wit.myweather.API.*
import org.wit.myweather.R
import org.wit.myweather.databinding.FragmentWeatherBinding
import org.wit.myweather.firebase.FirebaseDBManager.rand
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.models.WeatherTemperatureModel
import org.wit.myweather.ui.auth.LoggedInViewModel
import org.wit.myweather.webscraper.*
import kotlin.concurrent.thread


class WeatherFragment : Fragment() {

    lateinit var app: Main
    private var _fragBinding: FragmentWeatherBinding? = null
    private val fragBinding get() = _fragBinding!!
    var model = WeatherModel()
    var API = true
    private lateinit var weatherViewModel : WeatherViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

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

        activity?.title = getString(R.string.action_addweather)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        activateListeners()

        return root
    }

    private fun activateListeners(){
        addWeatherByText()
        addWeatherByURL()
        webscrape()
    }

    private fun webscrape(){
        //Web scraping disabled by Default
        fragBinding.AddWeatherURL.isEnabled = false
        fragBinding.AddWeatherURL.isClickable = false
        fragBinding.AddLink.isEnabled = false
        fragBinding.AddLink.isClickable = false

        //Toggling between API or Web Scraper
        fragBinding.checkBox.setOnClickListener{
            if(fragBinding.checkBox.isChecked == true){
                fragBinding.AddWeatherURL.isEnabled = true
                fragBinding.AddWeatherURL.isClickable = true
                fragBinding.AddLink.isEnabled = true
                fragBinding.AddLink.isClickable = true
                API = false
            }else{
                fragBinding.AddWeatherURL.isEnabled = false
                fragBinding.AddWeatherURL.isClickable = false
                fragBinding.AddLink.isEnabled = false
                fragBinding.AddLink.isClickable = false
                API = true
            }
        }
    }

    //Web Scraper function; Paste google link of weather choice.
    private fun addWeatherByURL(){
        fragBinding.AddWeatherURL.setOnClickListener {

                var location = getLocationByWebLink(fragBinding.AddLink.text.toString())
                var list = location.split(",")
                model.Country = list[1]
                model.City = list[0]
                model.WebLink = fragBinding.AddLink.text.toString()
                model.Type = "Scrape"
                model.Image = setImage(model.Country, model.County, model.City, model.WebLink)
                //Scrapes relevant info and sets respective Peak temperature.
                model.Temperature = getPeakTemp(model.Country, model.County, model.City)
                //Scrapes relevant info and sets respective Low temperature.
                model.TemperatureLow = getLowestTemp(model.Country, model.County, model.City)
                model.Favourite = false

            //Uploads Model to Firebase
            //Receives info from Firebase and saves locally
            create()
            //Navigate back
            weatherViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
                status?.let { render(status) }
            })

        }
    }
    //Add weather by API or Web Scraping.
    private fun addWeatherByText(){
        fragBinding.AddWeather.setOnClickListener {

            if(API == false) {
                if (fragBinding.AddCountry.text.isNotEmpty()) {
                    if (fragBinding.AddCity.text.isNotEmpty()) {

                            model.Country = fragBinding.AddCountry.text.toString()
                            model.County = fragBinding.AddCounty.text.toString()
                            model.City = fragBinding.AddCity.text.toString()
                            //Setting to type Scrape to enable web scraping
                            model.Type = "Scrape"
                            //Scrapes relevant info and sets respective image.
                            model.Image =
                                setImage(model.Country, model.County, model.City, model.WebLink)
                            //Scrapes relevant info and sets respective Peak temperature.
                            model.Temperature = getPeakTemp(model.Country, model.County, model.City)
                            //Scrapes relevant info and sets respective Low temperature.
                            model.TemperatureLow =
                                getLowestTemp(model.Country, model.County, model.City)
                            model.Favourite = false

                        //Uploads Model to Firebase
                        //Receives info from Firebase and saves locally
                        create()

                        val key =
                            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        key.hideSoftInputFromWindow(view?.getWindowToken(), 0)
                    } else {
                        Toast.makeText(
                            context, "Please fill in the city field.", Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(context, "Please fill in the country field.", Toast.LENGTH_LONG).show()
                }
            }
            if(API == true){

                if (fragBinding.AddCountry.text.isNotEmpty()) {
                    if (fragBinding.AddCity.text.isNotEmpty()) {

                            model.Country = fragBinding.AddCountry.text.toString()
                            model.County = fragBinding.AddCounty.text.toString()
                            model.City = fragBinding.AddCity.text.toString()

                            //API call to relevant info and sets respective image.
                            model.Image =
                                setIcon(model.Country, model.County, model.City).get(0)
                            //API call to relevant info and sets respective Peak temperature.
                            model.Temperature = getPeak(model.Country, model.County, model.City)
                            //API call to relevant info and sets respective Low temperature.
                            model.TemperatureLow =
                                getLow(model.Country, model.County, model.City)
                            //Uploads Model to Firebase
                            //Receives info from Firebase and saves locally

                        create()

                    }else {
                        Toast.makeText(
                            context, "Please fill in the city field.", Toast.LENGTH_SHORT
                        ).show()
                    }
                    }else {
                    Toast.makeText(context, "Please fill in the country field.", Toast.LENGTH_LONG).show()
                }

            }
            //Navigate back
            weatherViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
                status?.let { render(status) }
            })
        }
    }

    fun createWeatherTemperature(weatherModel: WeatherModel){
        thread {
            var peakTemplist = ArrayList<String>()
            var lowTemplist = ArrayList<String>()
            var weekDaylist = ArrayList<String>()
            var imagelist = ArrayList<String>()
            peakTemplist = getWeeklyPeak(weatherModel.Country, weatherModel.County, weatherModel.City)
            lowTemplist = getWeeklyLow(weatherModel.Country, weatherModel.County, weatherModel.City)
            weekDaylist = getWeekDays(weatherModel.Country, weatherModel.County, weatherModel.City)
            imagelist = getIconNameList(weatherModel.Country, weatherModel.County, weatherModel.City)
            //Upload to firebase Weather card weekly
            loggedInViewModel.uploadWeatherTemperature(
                WeatherTemperatureModel(weatherModel.id, weekDaylist, peakTemplist, lowTemplist, imagelist
            )
            )
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

    private fun create(){
        val uid = rand(500,1000000000)

        weatherViewModel.create(model.copy(id=uid.toLong()),loggedInViewModel.liveFirebaseUser)
        createWeatherTemperature(model.copy(id=uid.toLong()))
    }

    private fun render(status:Boolean){
        when (status) {
            true -> {
                    val action =
                        org.wit.myweather.ui.weather.WeatherFragmentDirections.actionWeatherFragmentToWeatherList()
                    findNavController().navigate((action))
            }
            false ->{
                Toast.makeText(context,getString(R.string.add_fail),Toast.LENGTH_LONG).show()
            }
        }
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