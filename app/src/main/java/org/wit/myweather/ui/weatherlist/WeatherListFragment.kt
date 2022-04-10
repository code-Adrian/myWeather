package org.wit.myweather.ui.weatherlist

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SwitchCompat

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.wit.myweather.API.getIconNameList

import org.wit.myweather.R
import org.wit.myweather.activities.*
import org.wit.myweather.databinding.FragmentWeatherListBinding
import org.wit.myweather.helpers.SwipeToDeleteCallback
import org.wit.myweather.helpers.SwipeToEditCallback
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.models.WeatherTemperatureModel
import org.wit.myweather.ui.auth.LoggedInViewModel
import org.wit.myweather.ui.weatheredit.WeatherEditViewModel
import org.wit.myweather.webscraper.getWeekDays
import org.wit.myweather.webscraper.getWeeklyPeakTemp
import org.wit.myweather.webscraper.getWeeklylowTemp
import kotlin.concurrent.thread


class WeatherListFragment : Fragment(),WeatherListener,FavListener {
    lateinit var app: Main
    private var _fragBinding: FragmentWeatherListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var weatherListViewModel : WeatherListViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    lateinit var adapter : WeatherAdapter
    private lateinit var weatherEditViewModel : WeatherEditViewModel

    var searchView: SearchView?=null

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
        weatherEditViewModel = ViewModelProvider(this).get(WeatherEditViewModel::class.java)

        loadWeather()
        swipeHandlers()
        setSwipeRefresh()
        return root
    }

    fun filter(menu: Menu) {

        val menuItem = menu.findItem(R.id.search_action).actionView as SearchView
        val searchManager: SearchManager = requireActivity().getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView = menuItem
        searchView!!.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
        searchView!!.maxWidth = Int.MAX_VALUE

        searchView!!.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {

                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                adapter.filter.filter(newText)
                return false
            }

        })
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
    //Toggle favourite
    override fun onFavClick(imageView: ImageView, weathermodel: WeatherModel) {
        if(imageView.drawable.constantState == resources.getDrawable(R.drawable.favourite).constantState) {
                imageView.setImageResource(R.drawable.notfavourite)
                weatherEditViewModel.update(weathermodel.copy(Favourite = false), loggedInViewModel.liveFirebaseUser
                )
        }else{
            imageView.setImageResource(R.drawable.favourite)
            weatherEditViewModel.update(weathermodel.copy(Favourite = true), loggedInViewModel.liveFirebaseUser)
        }
    }

//Live adapter
private fun loadWeather(){
    fragBinding.recyclerView.adapter?.notifyDataSetChanged()
    weatherListViewModel.observableWeatherList.observe(viewLifecycleOwner, Observer { weather ->
    weather?.let {
        render(weather as ArrayList<WeatherModel>)
        checkSwipeRefresh()
    }
    })

}

    //Filtering favourite
    private fun filterFavourites(menu: Menu){
        val item = menu.findItem(R.id.toggleFavourites) as MenuItem
        item.setActionView(R.layout.togglebutton_layout)
        val toggleFavourites: SwitchCompat = item.actionView.findViewById(R.id.toggleButton)
        toggleFavourites.isChecked = false
        toggleFavourites.setOnCheckedChangeListener{_, isChecked ->
          if(isChecked){
              weatherListViewModel.loadFavs()
          }else{
              weatherListViewModel.load()
          }
        }
    }
    //Recycle view
    private fun render(weather: ArrayList<WeatherModel>){
        fragBinding.recyclerView.setLayoutManager(LinearLayoutManager(activity))
        fragBinding.recyclerView.adapter = WeatherAdapter(weather as ArrayList<WeatherModel>,this,this)
        adapter = fragBinding.recyclerView.adapter as WeatherAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.weatherlist_menu,menu)
        filter(menu)
        filterFavourites(menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
        }

    private fun setSwipeRefresh() {
        fragBinding.swiperRefresh.setOnRefreshListener {
            fragBinding.swiperRefresh.isRefreshing = true
            weatherListViewModel.load()
        }
    }

    private fun checkSwipeRefresh() {
        if (fragBinding.swiperRefresh.isRefreshing)
            fragBinding.swiperRefresh.isRefreshing = false
    }

    private fun swipeHandlers(){
        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fragBinding.recyclerView.adapter as WeatherAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                weatherListViewModel.delete(viewHolder.itemView.tag as WeatherModel,loggedInViewModel.liveFirebaseUser)
                deleteWeatherTemperature(viewHolder.itemView.tag as WeatherModel)
            }
        }

        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)


        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val action = WeatherListFragmentDirections.actionWeatherListToWeatherEdit(
                        viewHolder.itemView.tag as WeatherModel
                    )
                findNavController().navigate((action))
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)
    }

    fun deleteWeatherTemperature(weatherModel: WeatherModel){
        thread {
            var peakTemplist = ArrayList<String>()
            var lowTemplist = ArrayList<String>()
            var weekDaylist = ArrayList<String>()
            var imagelist = ArrayList<String>()
            peakTemplist =
                getWeeklyPeakTemp(weatherModel.Country, weatherModel.County, weatherModel.City)
            lowTemplist =
                getWeeklylowTemp(weatherModel.Country, weatherModel.County, weatherModel.City)
            weekDaylist = getWeekDays(weatherModel.Country, weatherModel.County, weatherModel.City)
            //Image return from Weatherbit API, Web scraping Unreliable.
            imagelist =
                getIconNameList(weatherModel.Country, weatherModel.County, weatherModel.City)
            //Upload to firebase Weather card weekly
            loggedInViewModel.deleteWeatherTemperature(
                WeatherTemperatureModel(weatherModel.id, weekDaylist, peakTemplist, lowTemplist, imagelist
            )
            )
        }
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