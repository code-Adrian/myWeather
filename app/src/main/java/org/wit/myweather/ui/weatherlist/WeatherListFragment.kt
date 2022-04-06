package org.wit.myweather.ui.weatherlist

import android.os.Bundle
import android.view.*

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager

import org.wit.myweather.R
import org.wit.myweather.activities.*
import org.wit.myweather.databinding.FragmentWeatherListBinding
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.ui.weather.WeatherViewModel


class WeatherListFragment : Fragment(),WeatherListener,EditListener {
    lateinit var app: Main
    private var _fragBinding: FragmentWeatherListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var weatherListViewModel : WeatherListViewModel
    private lateinit var weatherViewModel: WeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as Main
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentWeatherListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_weathList)


        weatherListViewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)
        //weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        loadWeather()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onWeatherClick(weather: WeatherModel) {

        val action =
            WeatherListFragmentDirections.actionWeatherListFragmentToWeatherTemperatureFragment(
                weather
            )
        findNavController().navigate((action))
    }


    override fun onEditClick(weather: WeatherModel) {
        val action =
            WeatherListFragmentDirections.actionWeatherListToWeatherEdit(
                weather
            )
        findNavController().navigate((action))
    }


private fun loadWeather(){
    fragBinding.recyclerView.adapter?.notifyDataSetChanged()
    weatherListViewModel.observableWeatherList.observe(viewLifecycleOwner, Observer { weather ->
    weather?.let {
        render(weather as ArrayList<WeatherModel>)
    }
    })

}

    private fun render(weather: ArrayList<WeatherModel>){
        fragBinding.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        fragBinding.recyclerView.adapter = WeatherAdapter(weather as ArrayList<WeatherModel>,this,this)
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