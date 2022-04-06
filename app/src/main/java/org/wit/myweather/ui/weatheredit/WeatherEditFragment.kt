package org.wit.myweather.ui.weatheredit

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import org.wit.myweather.API.getLow
import org.wit.myweather.API.getPeak
import org.wit.myweather.API.setIcon
import org.wit.myweather.R
import org.wit.myweather.databinding.FragmentWeatherEditBinding
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.ui.auth.LoggedInViewModel
import org.wit.myweather.webscraper.getLowestTemp
import org.wit.myweather.webscraper.getPeakTemp
import org.wit.myweather.webscraper.setImage


class WeatherEditFragment : Fragment() {
    lateinit var app: Main
    private var _fragBinding: FragmentWeatherEditBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var model: WeatherModel
    private lateinit var weatherEditViewModel : WeatherEditViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as Main
        setHasOptionsMenu(true)

        //Receiving arguments from WeatherListFragment
        val bundle = arguments
        val getModel = bundle?.get("Model") as WeatherModel
        //Model getting added to Global variable model.
        model = getModel
        //Toast.makeText(context,model.id.toString(),Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        weatherEditViewModel = ViewModelProvider(this).get(WeatherEditViewModel::class.java)
        _fragBinding = FragmentWeatherEditBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_menu)


        fillText()
        updateListener()


    return root
    }

    private fun updateListener(){
        fragBinding.EditWeather.setOnClickListener {
            //Data Validation
            if(fragBinding.EditCountry.length() > 2) {
                if(fragBinding.EditCity.length() > 2) {

                    setModel()

                    //Update cloud
                    weatherEditViewModel.update(model.copy(),loggedInViewModel.liveFirebaseUser)
                    //Serialize locally by retrieving data from now updated cloud.
                    weatherEditViewModel.load()
                    //Navigate backl
                    weatherEditViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                        status -> status?.let { render(status) }
                    })

                }else
                {
                    Toast.makeText(context,"City field must not be less than 2 in length",Toast.LENGTH_LONG).show()
                }
            }else
            {
                Toast.makeText(context,"Country field must not be less than 3 in length",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun render(status:Boolean){
        when (status) {
            true -> {
            view?.let {
                val action =
                    org.wit.myweather.ui.weatheredit.WeatherEditFragmentDirections.actionWeatherEditToWeatherList()
                findNavController().navigate((action))
            }
            }
            false ->{
                Toast.makeText(context,getString(R.string.edit_fail),Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun fillText(){
        fragBinding.EditCountry.setText(model.Country)
        fragBinding.EditCounty.setText(model.County)
        fragBinding.EditCity.setText(model.City)
        fragBinding.EditLink.setText(model.WebLink)

    }

    private fun setModel(){
        //If the model is API, update data by API usage.
        if(model.Type.equals("API")){
            model.Country = fragBinding.EditCountry.text.toString()
            model.County = fragBinding.EditCounty.text.toString()
            model.City = fragBinding.EditCity.text.toString()
            model.WebLink = fragBinding.EditLink.text.toString()

            model.Image = setIcon(model.Country, model.County, model.City).get(0)
            //API relevant info and sets respective Peak temperature.
            model.Temperature = getPeak(model.Country, model.County, model.City)
            //API relevant info and sets respective Low temperature.
            model.TemperatureLow = getLow(model.Country, model.County, model.City)
        }
        //If the model is using Web scraping, update data by jSoup usage.
        if (model.Type.equals("Scrape")) {
            model.Country = fragBinding.EditCountry.text.toString()
            model.County = fragBinding.EditCounty.text.toString()
            model.City = fragBinding.EditCity.text.toString()
            model.WebLink = fragBinding.EditLink.text.toString()

            model.Image = setImage(model.Country, model.County, model.City, model.WebLink)
            //Scrapes relevant info and sets respective Peak temperature.
            model.Temperature = getPeakTemp(model.Country, model.County, model.City)
            //Scrapes relevant info and sets respective Low temperature.
            model.TemperatureLow = getLowestTemp(model.Country, model.County, model.City)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.weather_edit_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.edit_delete -> {
                weatherEditViewModel.delete(model.copy(),loggedInViewModel.liveFirebaseUser)
                weatherEditViewModel.load()
                weatherEditViewModel.observableWeatherEdit.observe(viewLifecycleOwner, Observer {weather ->
                    weather.remove(model.copy())
                })
                weatherEditViewModel.observableStatus.observe(viewLifecycleOwner, Observer {
                        status -> status?.let { render(status) }
                })
            }
        }
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)

    }

    companion object {

        @JvmStatic
        fun newInstance() =
            WeatherEditFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}