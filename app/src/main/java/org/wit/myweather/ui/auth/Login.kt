package org.wit.myweather.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.splash_screen.*
import org.wit.myweather.API.getIconNameList
import org.wit.myweather.API.getWeeklyLow
import org.wit.myweather.API.getWeeklyPeak
import org.wit.myweather.R
import org.wit.myweather.databinding.LoginBinding
import org.wit.myweather.models.WeatherTemperatureModel
import org.wit.myweather.ui.menu.MenuFragment
import org.wit.myweather.webscraper.getWeekDays
import org.wit.myweather.webscraper.getWeeklyPeakTemp
import org.wit.myweather.webscraper.getWeeklylowTemp

class Login: AppCompatActivity() {

    private lateinit var loginViewModel : LoginViewModel
    private lateinit var loginBinding: LoginBinding
    private lateinit var loggedInViewModel : LoggedInViewModel

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.splash_screen)
            splash_image.animate().setDuration(2000).alpha(1f).withEndAction {
                setContentView(loginBinding.root)
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            }
            loginBinding = LoginBinding.inflate(layoutInflater)

    }

    public override fun onStart() {
        super.onStart()

        loginViewModel = ViewModelProvider(this,viewModelFactory { LoginViewModel(this.application,this) }).get(LoginViewModel::class.java)
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)
        listeners()
        //loginViewModel.login()

        loginViewModel.liveFirebaseUser.observe(this, Observer
        { firebaseUser ->
            if (firebaseUser != null){
                //statement will disable triggering the preloading upon changing theme. -100 is only when app starts.
                    // 1 == NIGHT MODE ON. 2 == NIGHT MODE OFF
                if(AppCompatDelegate.getDefaultNightMode().toString().equals("-100") ) {
                    // ==== Disable this when testing to save API calls per day ======
                    //preloadWeatherTemperatureList()
                }


            }
        })
    }

    private fun listeners(){
        loginBinding.authbutton.setOnClickListener{
            loginViewModel.login()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


    private fun preloadWeatherTemperatureList(){

        //Trigger
        loggedInViewModel.getWeatherModels()
        //Observe trigger
        loggedInViewModel.observableWeather.observe(this,{ allweatherlist ->
            for(i in allweatherlist){
                //Iterate through all and upload each
                //thread{
                    if(i.Type.equals("Scrape")) {
                        var peakTemplist = ArrayList<String>()
                        var lowTemplist = ArrayList<String>()
                        var weekDaylist = ArrayList<String>()
                        var imagelist = ArrayList<String>()
                        peakTemplist = getWeeklyPeakTemp(i.Country, i.County, i.City)
                        lowTemplist = getWeeklylowTemp(i.Country, i.County, i.City)
                        weekDaylist = getWeekDays(i.Country, i.County, i.City)
                        //Image return from Weatherbit API, Web scraping Unreliable.
                        imagelist = getIconNameList(i.Country, i.County, i.City)
                        //Upload to firebase Weather card weekly
                        loggedInViewModel.uploadWeatherTemperature(WeatherTemperatureModel(i.id, weekDaylist, peakTemplist, lowTemplist, imagelist))
                    }
              //  }
                if(i.Type.equals("API")) {
                        var peakTemplist = ArrayList<String>()
                        var lowTemplist = ArrayList<String>()
                        var weekDaylist = ArrayList<String>()
                        var imagelist = ArrayList<String>()
                        peakTemplist = getWeeklyPeak(i.Country, i.County, i.City)
                        lowTemplist = getWeeklyLow(i.Country, i.County, i.City)
                        weekDaylist = getWeekDays(i.Country, i.County, i.City)
                        imagelist = getIconNameList(i.Country, i.County, i.City)
                        //Upload to firebase weather card weekly
                        loggedInViewModel.uploadWeatherTemperature(WeatherTemperatureModel(i.id, weekDaylist, peakTemplist, lowTemplist, imagelist))
                }
              }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println(requestCode)
    }

    //Create factory func for viewModel to pass extra parameter
    protected inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(aClass: Class<T>):T = f() as T
        }

    override fun onStop() {
        super.onStop()
    }

}