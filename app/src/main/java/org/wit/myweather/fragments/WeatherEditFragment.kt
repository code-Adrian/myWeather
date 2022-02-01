package org.wit.myweather.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import org.jetbrains.anko.startActivityForResult
import org.wit.myweather.R
import org.wit.myweather.activities.WeatherListActivity
import org.wit.myweather.databinding.FragmentWeatherEditBinding
import org.wit.myweather.main.Main
import org.wit.myweather.models.WeatherModel
import kotlin.concurrent.thread


class WeatherEditFragment : Fragment() {
    lateinit var app: Main
    private var _fragBinding: FragmentWeatherEditBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var model: WeatherModel

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
        _fragBinding = FragmentWeatherEditBinding.inflate(inflater, container, false)
        val root = fragBinding.root


        val toolbar = root.findViewById<Toolbar>(R.id.toolbarEdit)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        activity?.title = getString(R.string.action_menu)
       // Toast.makeText(context,"Work?",Toast.LENGTH_SHORT).show()

        fillText()
        fragBinding.EditWeather.setOnClickListener {

            setModel()
            app.weather.update(model.copy())
            thread {
                val action = WeatherEditFragmentDirections.actionWeatherEditToWeatherList()
                findNavController().navigate((action))
            }
        }
    return root
    }

    private fun fillText(){
        fragBinding.EditCountry.setText(model.Country)
        fragBinding.EditCounty.setText(model.County)
        fragBinding.EditCity.setText(model.City)
        fragBinding.EditLink.setText(model.WebLink)

    }

    private fun setModel(){
        model.Country = fragBinding.EditCountry.text.toString()
        model.County = fragBinding.EditCounty.text.toString()
        model.City = fragBinding.EditCity.text.toString()
        model.WebLink = fragBinding.EditLink.text.toString()

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
                app.weather.delete(model)
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