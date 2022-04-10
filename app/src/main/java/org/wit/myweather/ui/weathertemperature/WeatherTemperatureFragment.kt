package org.wit.myweather.ui.weathertemperature

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import org.wit.myweather.API.getWeeklyLow
import org.wit.myweather.API.getWeeklyPeak
import org.wit.myweather.API.setIcon
import org.wit.myweather.R
import org.wit.myweather.databinding.FragmentWeatherTemperatureBinding
import org.wit.myweather.firebase.FirebaseStorageManager
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.ui.auth.LoggedInViewModel
import org.wit.myweather.ui.weather.WeatherViewModel
import org.wit.myweather.webscraper.*

class WeatherTemperatureFragment : Fragment() {

    lateinit var app: Main
    private var _fragBinding: FragmentWeatherTemperatureBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var model: WeatherModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private lateinit var weathertempViewModel: WeatherTemperatureViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = activity?.application as Main
        setHasOptionsMenu(true)
        activity?.title = getString(R.string.action_weekstats)
        //Receiving arguments from WeatherListFragment
        val bundle = arguments
        val getModel = bundle?.get("model") as WeatherModel
        //Model getting added to Global variable model.
        model = getModel
        weathertempViewModel = ViewModelProvider(this).get(WeatherTemperatureViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentWeatherTemperatureBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        setDetails()
        return root
    }

    private fun setDetails() {
        var peakTempArr = arrayOf(fragBinding.card1PeakTemp, fragBinding.card2PeakTemp, fragBinding.card3PeakTemp, fragBinding.card4PeakTemp, fragBinding.card5PeakTemp, fragBinding.card6PeakTemp, fragBinding.card7PeakTemp, fragBinding.card8PeakTemp)
        var lowTempArr = arrayOf(fragBinding.card1LowTemp, fragBinding.card2LowTemp, fragBinding.card3LowTemp, fragBinding.card4LowTemp, fragBinding.card5LowTemp, fragBinding.card6LowTemp, fragBinding.card7LowTemp, fragBinding.card8LowTemp)
        var dayArr = arrayOf(fragBinding.card2Day, fragBinding.card3Day, fragBinding.card4Day, fragBinding.card5Day, fragBinding.card6Day, fragBinding.card7Day, fragBinding.card8Day)
        var imageArr = arrayOf(fragBinding.card1Image, fragBinding.card2Image, fragBinding.card3Image, fragBinding.card4Image, fragBinding.card5Image, fragBinding.card6Image, fragBinding.card7Image, fragBinding.card8Image)

        if (model.Type.equals("Scrape")) {
            weathertempViewModel.getSpecifiedTempModel(model.id.toString())
            weathertempViewModel.observableWeather.observe(viewLifecycleOwner, { weather ->
                //Peak Temp
                for (i in 0..7) {
                    peakTempArr.get(i).text = weather.get(0).PeakTemperature.get(i) + "°C"
                }
                //Low Temp
                for (i in 0..7) {
                    lowTempArr.get(i).text = weather.get(0).MinimumTemperature.get(i) + "°C"
                }
                //Day
                for (i in 0..6) {
                    dayArr.get(i).text = weather.get(0).WeekDay.get(i)
                }

                //FirebaseStorageManager.checkforexistingimagetoList(imagelist, "png")
                for (i in 0..7) {

                    val imageRef =
                        FirebaseStorageManager.storage.child(weather.get(0).ImageName.get(i) + ".png")
                    imageRef.downloadUrl.addOnCompleteListener { url ->
                        Glide.with(fragBinding.root).load(url.result!!.toString())
                            .into(imageArr.get(i))
                    }
                }
            })
        }

        if (model.Type.equals("API")) {
            weathertempViewModel.getSpecifiedTempModel(model.id.toString())
            weathertempViewModel.observableWeather.observe(viewLifecycleOwner, { weather ->
                //Peak Temp
                for (i in 0..7) {
                    peakTempArr.get(i).text = weather.get(0).PeakTemperature.get(i) + "°C"
                }
                //Low Temp
                for (i in 0..7) {
                    lowTempArr.get(i).text = weather.get(0).MinimumTemperature.get(i) + "°C"
                }
                //Day
                for (i in 0..6) {
                    dayArr.get(i).text = weather.get(0).WeekDay.get(i)
                }

                //FirebaseStorageManager.checkforexistingimagetoList(imagelist, "png")
                for (i in 0..7) {

                    val imageRef = FirebaseStorageManager.storage.child(weather.get(0).ImageName.get(i)+".png")
                    imageRef.downloadUrl.addOnCompleteListener{url ->
                        Glide.with(fragBinding.root).load(url.result!!.toString()).into(imageArr.get(i))
                    }
                }

            })

        }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.weather_temperature,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)

    }


    companion object {

        @JvmStatic
        fun newInstance() =
            WeatherTemperatureFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}