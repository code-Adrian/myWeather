package org.wit.myweather.mvvm

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.wit.myweather.models.MainMenuDetailsModel

class MenuViewModel : ViewModel(){

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

    private fun updateLiveData(set: MainMenuDetailsModel) {
        setMainMenuDetails(set)
    }





}