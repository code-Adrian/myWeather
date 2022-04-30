package org.wit.myweather.ui.maps

import android.annotation.SuppressLint
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.myweather.API.*
import org.wit.myweather.R
import org.wit.myweather.databinding.FragmentMapsBinding
import org.wit.myweather.databinding.FragmentMenuBinding
import org.wit.myweather.firebase.FirebaseDBManager
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.models.WeatherTemperatureModel
import org.wit.myweather.ui.auth.LoggedInViewModel
import org.wit.myweather.ui.weather.WeatherViewModel
import org.wit.myweather.webscraper.getWeekDays
import org.wit.myweather.webscraper.getWeeklyPeakTemp
import org.wit.myweather.webscraper.getWeeklylowTemp
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class MapsFragment : Fragment() {

    private var _fragBinding: FragmentMapsBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    private lateinit var weatherViewModel : WeatherViewModel

    private val mapsViewModel: MapsViewModel by activityViewModels()
    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        mapsViewModel.map = googleMap
        mapsViewModel.map.isMyLocationEnabled = true

            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )


        mapsViewModel.map.uiSettings.setAllGesturesEnabled(true)
        mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
        mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true
        mapsViewModel.map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14f))
        mapsViewModel.currentMarkerLocation.value = loc

            mapsViewModel.map.addMarker(
                MarkerOptions().position(loc).title("Im Here!").snippet(geoCoder(loc)).draggable(true)
            )?.showInfoWindow()
            mapsViewModel.map.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
                override fun onMarkerDrag(p0: Marker) {

                }
                override fun onMarkerDragEnd(p0: Marker) {
                    drawMarker(p0.position, p0)
                    mapsViewModel.currentMarkerLocation.value = p0.position
                }
                override fun onMarkerDragStart(p0: Marker) {
                }

            })
    }

    fun addWeather(){
        fragBinding.AddWeatherMAP.setOnClickListener{
           var location = mapsViewModel.currentMarkerLocation.value
            if(location != null){
                var mainmodel = WeatherModel()
                var model1 = getWeatherByLatLon(location.latitude.toString(),location.longitude.toString())
                var model2 = geoCoderDetail(location)
                //Model1
                mainmodel.Temperature = model1.Temperature
                mainmodel.TemperatureLow = model1.TemperatureLow
                mainmodel.Image = model1.Image
                //Model2
                mainmodel.Country = getCountryQuery(location.longitude.toString(),location.latitude.toString())
                mainmodel.County = getRegionQuery(location.longitude.toString(),location.latitude.toString())
                //mainmodel.County.replace(" ","")
                //mainmodel.City = model2.City.replace(" ","")
                mainmodel.City = getCityQuery(location.longitude.toString(),location.latitude.toString())
                println(mainmodel)
                //Creating Weather Model & Weather Temperature Model on firebase.
                if(mainmodel.Country.isNotEmpty() && mainmodel.County.isNotEmpty() && mainmodel.City.isNotEmpty()) {
                    create(mainmodel)
                }
            }
        }
    }

    fun drawMarker(loc: LatLng,marker: Marker){
        marker.remove()
        mapsViewModel.map.addMarker(
            MarkerOptions().position(loc).title("Im Here!").snippet(bigDataLocationCoder(loc)).draggable(true).visible(true)
        )?.showInfoWindow()
    }

    fun geoCoder(loc: LatLng) : String{
        val geoCoder = Geocoder(requireContext(), Locale.getDefault())

        val address = geoCoder.getFromLocation(loc.latitude,loc.longitude,1)[0].getAddressLine(0)
        return address
    }

    fun bigDataLocationCoder(loc: LatLng) : String{
        return getCountryQuery(loc.longitude.toString(),loc.latitude.toString()) +", "+ getRegionQuery(loc.longitude.toString(),loc.latitude.toString()) +", "+ getCityQuery(loc.longitude.toString(),loc.latitude.toString())
    }

    private fun create(model: WeatherModel){
        fragBinding.AddWeatherMAP.isEnabled = false
       Toast.makeText(context,"Loading please wait......",Toast.LENGTH_SHORT).show()
        val uid = FirebaseDBManager.rand(500, 1000000000)
        var bool = createWeatherTemperature(model.copy(id=uid.toLong()))
        if(bool == true) {
            weatherViewModel.create(model.copy(id = uid.toLong()), loggedInViewModel.liveFirebaseUser)
            fragBinding.AddWeatherMAP.isEnabled = true
            val action = org.wit.myweather.ui.maps.MapsFragmentDirections.actionMapsFragmentToWeatherListFragment()
            findNavController().navigate((action))
        }else{
            Toast.makeText(context,"Location could not be added, try different location.",Toast.LENGTH_SHORT).show()
            fragBinding.AddWeatherMAP.isEnabled = true
        }

    }

    fun createWeatherTemperature(weatherModel: WeatherModel) : Boolean{
        var peakTemplist = ArrayList<String>()
        var lowTemplist = ArrayList<String>()
        var weekDaylist = ArrayList<String>()
        var imagelist = ArrayList<String>()
        peakTemplist = getWeeklyPeak(weatherModel.Country, weatherModel.County, weatherModel.City)
        lowTemplist = getWeeklyLow(weatherModel.Country, weatherModel.County, weatherModel.City)
        weekDaylist = getWeekDays(weatherModel.Country, weatherModel.County, weatherModel.City)
        imagelist = getIconNameList(weatherModel.Country, weatherModel.County, weatherModel.City)
        println(peakTemplist.size)
        println(lowTemplist.size)
        println(weekDaylist.size)
        println(imagelist.size)
            if(peakTemplist.size == 8 && lowTemplist.size == 8 && weekDaylist.size == 8 && imagelist.size == 8) {
                //Upload to firebase Weather card weekly
                loggedInViewModel.uploadWeatherTemperature(
                    WeatherTemperatureModel(
                        weatherModel.id, weekDaylist, peakTemplist, lowTemplist, imagelist
                    )
                )
                return true
            }else{
                return false
            }
    }

    fun geoCoderDetail(loc: LatLng) : WeatherModel{
        var model = WeatherModel()

        var county = ""
        var city = ""
        try {
            val geoCoder = Geocoder(requireContext(), Locale.getDefault())
            val country = geoCoder.getFromLocation(loc.latitude, loc.longitude, 1)[0].countryName
            var alldetails =
                geoCoder.getFromLocation(loc.latitude, loc.longitude, 1)[0].getAddressLine(0)
                    .toString()
            var details = alldetails.split(",")
             county = details.get(details.size - 2)
             city = details.get(details.size - 3)
            model.Country = country
            model.County = county
            model.City = city
        }catch(e:Exception){}

        return model
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentMapsBinding.inflate(inflater, container, false)

        addWeather()
        val root = fragBinding.root
        activity?.title = getString(R.string.mapFragmentTitle)
        weatherViewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}