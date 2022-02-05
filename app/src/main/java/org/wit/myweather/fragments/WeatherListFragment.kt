package org.wit.myweather.fragments

import android.os.Bundle
import android.os.StrictMode
import android.view.*
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.myweather.R
import org.wit.myweather.activities.*
import org.wit.myweather.databinding.FragmentWeatherListBinding
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.webscraper.getDateDay
import org.wit.myweather.webscraper.getLocation
import org.wit.myweather.webscraper.getPeakTemp
import org.wit.myweather.webscraper.getWeatherStatus
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread


class WeatherListFragment : Fragment(),WeatherListener,EditListener {
    lateinit var app: Main
    private var _fragBinding: FragmentWeatherListBinding? = null
    private val fragBinding get() = _fragBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as Main
        setHasOptionsMenu(true)
        getWeatherAsync()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentWeatherListBinding.inflate(inflater, container, false)



        val root = fragBinding.root

        activity?.title = getString(R.string.action_weathList)
        //fragBinding.toolbar.setBackgroundResource(R.drawable.weather_card_gradient)
        loadWeather()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        //loadWeather()

    }

    override fun onWeatherClick(weather: WeatherModel) {
        val action = WeatherListFragmentDirections.actionWeatherListFragmentToWeatherTemperatureFragment(weather)
        findNavController().navigate((action))
       // Toast.makeText(context,weather.City,Toast.LENGTH_SHORT).show()
    }


    override fun onEditClick(weather: WeatherModel) {
        val action = WeatherListFragmentDirections.actionWeatherListToWeatherEdit(weather)
        findNavController().navigate((action))
    }

    val queue = LinkedBlockingQueue<MutableList<WeatherModel>>()
    private fun getWeatherAsync(){
        Thread {
            queue.add(app.weather.getAll())
        }.start()

    }
private fun loadWeather(){

   thread {
       fragBinding.recyclerView.setLayoutManager(LinearLayoutManager(activity))
   }

    fragBinding.recyclerView.adapter = WeatherAdapter(queue.take(),this,this)
    //fragBinding.recyclerView.adapter?.notifyDataSetChanged()
}


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.weatherlist_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)

        }



    companion object {
        @JvmStatic
        fun newInstance() =
            WeatherListFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }


}