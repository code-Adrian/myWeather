package org.wit.myweather.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.android.synthetic.main.fragment_menu.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.wit.myweather.API.getPeakCoordinated
import org.wit.myweather.API.setIcon
import org.wit.myweather.API.setIconCoordinated
import org.wit.myweather.R
import org.wit.myweather.databinding.FragmentMenuBinding
import org.wit.myweather.livedata.ViewModel
import org.wit.myweather.main.Main
import org.wit.myweather.models.Json_Store
import org.wit.myweather.models.MainMenuDetailsModel
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.webscraper.getDateDay
import org.wit.myweather.webscraper.getLocation
import org.wit.myweather.webscraper.getPeakTemp
import org.wit.myweather.webscraper.getWeatherStatus
import kotlin.concurrent.thread
import kotlin.random.Random


class MenuFragment : Fragment(), EasyPermissions.PermissionCallbacks{

    lateinit var app: Main
    private var _fragBinding: FragmentMenuBinding? = null
    private val fragBinding get() = _fragBinding!!
    //Live data
    private val viewModel: ViewModel by viewModels()
    //Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as Main
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _fragBinding = FragmentMenuBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_menu)
        fragBinding.progressBar.visibility = View.INVISIBLE

        setDetails()

        fragBinding.menuButton.setOnClickListener{
                val action = MenuFragmentDirections.actionMenuFragmentToWeatherList()
                findNavController().navigate((action))
            fragBinding.progressBar.visibility = View.VISIBLE
        }


        return root
    }
    @SuppressLint("MissingPermission")
fun setDetails(){
    if(hasLocationPermission()){

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            //Physical Device, Using Live Data MVVM, API

                if (location != null) {
                    thread {
                        try {
                            val latitude = location.latitude
                            val longitude = location.longitude

                            val geoCoder = Geocoder(requireContext())
                            val currentLocation =
                                geoCoder.getFromLocation(latitude, longitude, 1).first().locality


                            val view = viewModel
                            val setter = MainMenuDetailsModel(
                                currentLocation,
                                getPeakCoordinated(
                                    latitude.toString(),
                                    longitude.toString()
                                ) + "°C",
                                getDateDay(),
                                setIconCoordinated(
                                    latitude.toString(),
                                    longitude.toString()
                                ).toString()
                            )

                            view.loop(setter)

                        }catch (e: Exception){
                            //If lacality can not be found, print error and use web scraping.
                            println(e.printStackTrace())
                            val view = viewModel
                            val setter = MainMenuDetailsModel(
                                getLocation(),
                                getPeakTemp(),
                                getDateDay(),
                                getWeatherStatus().toString()
                            )
                            view.loop(setter)
                        }
                    }
                    //If in emulator
                } else {
                    thread {
                        val view = viewModel
                        val setter = MainMenuDetailsModel(
                            getLocation(),
                            getPeakTemp(),
                            getDateDay(),
                            getWeatherStatus().toString()
                        )
                        view.loop(setter)
                    }
                }
            observer()
        }
    }else{
        requestLocationPermission()
    }
}

    private fun preloadWeather(){
        app.localWeather.serialize(app.weather.getAll())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    override fun onResume() {
        super.onResume()
        preloadWeather()
        setDetails()

        fragBinding.progressBar.visibility = View.INVISIBLE
    }


    private fun observer(){
        viewModel.getMainMenuDetails().observe(viewLifecycleOwner,{mainmenu ->
            fragBinding.menuTemp.text = mainmenu.temperature
            fragBinding.menuCounty.text = mainmenu.location
            fragBinding.menuDayofweek.text = mainmenu.weekDay
            fragBinding.weatherIcon.setImageResource(mainmenu.image.toInt())
            fragBinding.progressBar.visibility = View.INVISIBLE
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MenuFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    //                               ==========  PERMISIONS SECTION ==========                    \\

    private fun hasLocationPermission() = EasyPermissions.hasPermissions(requireContext(),Manifest.permission.ACCESS_FINE_LOCATION)

    private fun requestLocationPermission(){
        EasyPermissions.requestPermissions(this,"This application does not function without Location Permission",1,Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        //Give the user an option to choose permissions
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms.first())){
            SettingsDialog.Builder(requireActivity()).build().show()
        }else{
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(context,"Permissions Granted!",Toast.LENGTH_SHORT).show()
    }

    //                               ==========  END ==========                                   \\
}