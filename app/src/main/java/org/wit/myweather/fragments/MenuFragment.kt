package org.wit.myweather.fragments

import android.os.Bundle
import android.os.StrictMode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.mainmenu_activity.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.uiThread
import org.wit.myweather.R
import org.wit.myweather.activities.WeatherListActivity
import org.wit.myweather.databinding.FragmentMenuBinding
import org.wit.myweather.main.Main
import org.wit.myweather.models.Communicator
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.webscraper.getDateDay
import org.wit.myweather.webscraper.getLocation
import org.wit.myweather.webscraper.getPeakTemp
import org.wit.myweather.webscraper.getWeatherStatus
import kotlin.concurrent.thread


class MenuFragment : Fragment(){

    lateinit var app: Main
    private var _fragBinding: FragmentMenuBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as Main


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentMenuBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_menu)
        loadDetails()


        fragBinding.progressBar.visibility = View.INVISIBLE

        fragBinding.menuButton.setOnClickListener{
            thread {
                val action = MenuFragmentDirections.actionMenuFragmentToWeatherList()
                findNavController().navigate((action))
            }
            fragBinding.progressBar.visibility = View.VISIBLE

        }


        return root
    }


    private fun preloadWeather(){
        app.weather.getAll()

    }

    private fun loadDetails(){
        doAsync {
            uiThread {
                var policy = StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                fragBinding.menuCounty.text= getLocation()
                fragBinding.menuDayofweek.text = getDateDay()
                fragBinding.menuTemp.text = getPeakTemp()
                fragBinding.weatherIcon.setBackgroundResource(getWeatherStatus())
                fragBinding.progressBar.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        preloadWeather()

        //Toast.makeText(context,"asdadd",Toast.LENGTH_SHORT).show()
        fragBinding.progressBar.visibility = View.VISIBLE
    }



    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}