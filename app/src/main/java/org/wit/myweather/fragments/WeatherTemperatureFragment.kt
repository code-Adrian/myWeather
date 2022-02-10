package org.wit.myweather.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.myweather.API.getWeeklyLow
import org.wit.myweather.API.getWeeklyPeak
import org.wit.myweather.API.setIcon
import org.wit.myweather.R
import org.wit.myweather.databinding.FragmentMenuBinding
import org.wit.myweather.databinding.FragmentWeatherTemperatureBinding
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.webscraper.*

class WeatherTemperatureFragment : Fragment() {

    lateinit var app: Main
    private var _fragBinding: FragmentWeatherTemperatureBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var model: WeatherModel

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

    private fun setDetails(){

        doAsync {
            uiThread {
                if(model.Type.equals("Scrape") ){
                        var peakTemplist = ArrayList<String>()
                        var lowTemplist = ArrayList<String>()
                        var weekDaylist = ArrayList<String>()
                        var imagelist = ArrayList<Int>()
                        peakTemplist = getWeeklyPeakTemp(model.Country, model.County, model.City)
                        lowTemplist = getWeeklylowTemp(model.Country, model.County, model.City)
                        weekDaylist = getWeekDays(model.Country, model.County, model.City)
                        imagelist = getWeeklyWeather(model.Country, model.County, model.City)


                        fragBinding.card1PeakTemp.text = peakTemplist.get(0)
                        fragBinding.card2PeakTemp.text = peakTemplist.get(1)
                        fragBinding.card3PeakTemp.text = peakTemplist.get(2)
                        fragBinding.card4PeakTemp.text = peakTemplist.get(3)
                        fragBinding.card5PeakTemp.text = peakTemplist.get(4)
                        fragBinding.card6PeakTemp.text = peakTemplist.get(5)
                        fragBinding.card7PeakTemp.text = peakTemplist.get(6)
                        fragBinding.card8PeakTemp.text = peakTemplist.get(7)

                        fragBinding.card1LowTemp.text = lowTemplist.get(0)
                        fragBinding.card2LowTemp.text = lowTemplist.get(1)
                        fragBinding.card3LowTemp.text = lowTemplist.get(2)
                        fragBinding.card4LowTemp.text = lowTemplist.get(3)
                        fragBinding.card5LowTemp.text = lowTemplist.get(4)
                        fragBinding.card6LowTemp.text = lowTemplist.get(5)
                        fragBinding.card7LowTemp.text = lowTemplist.get(6)
                        fragBinding.card8LowTemp.text = lowTemplist.get(7)


                        fragBinding.card2Day.text = weekDaylist.get(1)
                        fragBinding.card3Day.text = weekDaylist.get(2)
                        fragBinding.card4Day.text = weekDaylist.get(3)
                        fragBinding.card5Day.text = weekDaylist.get(4)
                        fragBinding.card6Day.text = weekDaylist.get(5)
                        fragBinding.card7Day.text = weekDaylist.get(6)
                        fragBinding.card8Day.text = weekDaylist.get(7)

                        fragBinding.card1Image.setImageResource(
                            setImage(
                                model.Country,
                                model.County,
                                model.City,
                                model.WebLink
                            )
                        )
                        fragBinding.card2Image.setImageResource(imagelist.get(1))
                        fragBinding.card3Image.setImageResource(imagelist.get(2))
                        fragBinding.card4Image.setImageResource(imagelist.get(3))
                        fragBinding.card5Image.setImageResource(imagelist.get(4))
                        fragBinding.card6Image.setImageResource(imagelist.get(5))
                        fragBinding.card7Image.setImageResource(imagelist.get(6))
                        fragBinding.card8Image.setImageResource(imagelist.get(7))
                    }

                if(model.Type.equals("API") ){
                    var peakTemplist = ArrayList<String>()
                    var lowTemplist = ArrayList<String>()
                    var weekDaylist = ArrayList<String>()
                    var imagelist = ArrayList<Int>()
                    peakTemplist = getWeeklyPeak(model.Country, model.County, model.City)
                    lowTemplist = getWeeklyLow(model.Country, model.County, model.City)
                    weekDaylist = getWeekDays(model.Country, model.County, model.City)
                    imagelist = setIcon(model.Country, model.County, model.City)



                    fragBinding.card1PeakTemp.text = peakTemplist.get(0)+"°C"
                    fragBinding.card2PeakTemp.text = peakTemplist.get(1)+"°C"
                    fragBinding.card3PeakTemp.text = peakTemplist.get(2)+"°C"
                    fragBinding.card4PeakTemp.text = peakTemplist.get(3)+"°C"
                    fragBinding.card5PeakTemp.text = peakTemplist.get(4)+"°C"
                    fragBinding.card6PeakTemp.text = peakTemplist.get(5)+"°C"
                    fragBinding.card7PeakTemp.text = peakTemplist.get(6)+"°C"
                    fragBinding.card8PeakTemp.text = peakTemplist.get(7)+"°C"

                    fragBinding.card1LowTemp.text = lowTemplist.get(0)+"°C"
                    fragBinding.card2LowTemp.text = lowTemplist.get(1)+"°C"
                    fragBinding.card3LowTemp.text = lowTemplist.get(2)+"°C"
                    fragBinding.card4LowTemp.text = lowTemplist.get(3)+"°C"
                    fragBinding.card5LowTemp.text = lowTemplist.get(4)+"°C"
                    fragBinding.card6LowTemp.text = lowTemplist.get(5)+"°C"
                    fragBinding.card7LowTemp.text = lowTemplist.get(6)+"°C"
                    fragBinding.card8LowTemp.text = lowTemplist.get(7)+"°C"

                    fragBinding.card2Day.text = weekDaylist.get(1)
                    fragBinding.card3Day.text = weekDaylist.get(2)
                    fragBinding.card4Day.text = weekDaylist.get(3)
                    fragBinding.card5Day.text = weekDaylist.get(4)
                    fragBinding.card6Day.text = weekDaylist.get(5)
                    fragBinding.card7Day.text = weekDaylist.get(6)
                    fragBinding.card8Day.text = weekDaylist.get(7)

                    fragBinding.card1Image.setImageResource(imagelist.get(0))
                    fragBinding.card2Image.setImageResource(imagelist.get(1))
                    fragBinding.card3Image.setImageResource(imagelist.get(2))
                    fragBinding.card4Image.setImageResource(imagelist.get(3))
                    fragBinding.card5Image.setImageResource(imagelist.get(4))
                    fragBinding.card6Image.setImageResource(imagelist.get(5))
                    fragBinding.card7Image.setImageResource(imagelist.get(6))
                    fragBinding.card8Image.setImageResource(imagelist.get(7))





                }
            }
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