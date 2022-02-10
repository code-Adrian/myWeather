package org.wit.myweather.livedata

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.myweather.fragments.MenuFragment
import org.wit.myweather.main.Main
import org.wit.myweather.models.MainMenuDetailsModel
import org.wit.myweather.webscraper.getDateDay
import org.wit.myweather.webscraper.getLocation
import java.time.LocalDate
import kotlin.random.Random

class ViewModel : ViewModel(){

    private val mainmenu = MutableLiveData<MainMenuDetailsModel>()
    private val delay = 3000L

  //  init {
    //    loop()
   // }

    fun getMainMenuDetails() : LiveData<MainMenuDetailsModel> = mainmenu

    fun setMainMenuDetails(set : MainMenuDetailsModel) {
        mainmenu.value = set
    }


    fun loop(set: MainMenuDetailsModel){
        //Handler manages on a different thread that allows data being passed
        //Handlers work with Loopers thread.
        //Post Delay is used by live data when on a background thread.
        Handler(Looper.getMainLooper()).postDelayed({

            updateLiveData(set)
        },100L)
    }

    private fun updateLiveData(set: MainMenuDetailsModel){
      //  var temp = ""
       // var day = getDateDay()
     //   var loc = getLocation()
     //  var image = ""

       // val lo = MainMenuDetailsModel(loc,temp,day,image)
        setMainMenuDetails(set)
        //Repeat
       // loop()
    }



}