package org.wit.myweather.activities

import android.content.Intent
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.nav_header.view.*
import org.wit.myweather.API.getIconNameList
import org.wit.myweather.API.getWeeklyLow
import org.wit.myweather.API.getWeeklyPeak
import org.wit.myweather.R
import org.wit.myweather.databinding.HomeBinding
import org.wit.myweather.databinding.NavHeaderBinding
import org.wit.myweather.firebase.FirebaseStorageManager
import org.wit.myweather.firebase.FirebaseStorageManager.checkforexistingimage
import org.wit.myweather.helpers.readImageUri
import org.wit.myweather.helpers.showImagePicker
import org.wit.myweather.models.WeatherTemperatureModel
import org.wit.myweather.ui.auth.LoggedInViewModel
import org.wit.myweather.ui.auth.Login
import org.wit.myweather.ui.maps.MapsViewModel
import org.wit.myweather.webscraper.getWeekDays
import org.wit.myweather.webscraper.getWeeklyPeakTemp
import org.wit.myweather.webscraper.getWeeklylowTemp
import java.lang.Exception
import kotlin.concurrent.thread

class home : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding: HomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loggedInViewModel : LoggedInViewModel
    private lateinit var navHeaderBinding : NavHeaderBinding
    lateinit var navView : NavigationView
    private lateinit var intentLauncher : ActivityResultLauncher<Intent>
    private val mapsViewModel : MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        navView = homeBinding.navView

        navView.setupWithNavController(navController)
        changeTheme()
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.menuFragment, R.id.weatherListFragment,R.id.weatherFragment,R.id.weatherTemperatureFragment,R.id.weatherEditFragment,R.id.mapsFragment), drawerLayout)
            setupActionBarWithNavController(navController, appBarConfiguration)
        signOut()

    }

    override fun onStart() {
        super.onStart()

        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)

        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null) {
                val currentUser = loggedInViewModel.liveFirebaseUser.value
                if (currentUser != null){ updateNavHeader(loggedInViewModel.liveFirebaseUser.value!!)}

            }
        })
        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                startActivity(Intent(this, Login::class.java))
            }
        })


        registerImagePickerCallback()
        updateMapLocation()
    }


    private fun updateMapLocation(){
        mapsViewModel.updateCurrentLocation()
    }



    private fun updateNavHeader(currentUser: FirebaseUser) {
        //Checking if profile picture is already out there and causing an update/change

        var headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
        if(currentUser.isAnonymous){

            navHeaderBinding.emailText.text = "Anonymous"
            imageuriobserver()
        }
        if (currentUser.isEmailVerified){
            navHeaderBinding.emailText.text = currentUser.email
            try {
                //Update with existing photo for account
                Glide.with(this).load(currentUser!!.photoUrl).into(navHeaderBinding.imageViewLoggedIn)
            }catch (e: Exception){
                println("Glide Exception ${e}")
            }
        }else{
            if(!currentUser.phoneNumber.isNullOrEmpty()){
                navHeaderBinding.emailText.text = currentUser.phoneNumber
                imageuriobserver()
            }
            if(!currentUser.displayName.isNullOrEmpty()){
                navHeaderBinding.emailText.text = currentUser.displayName
                //Observes imageURI, if its empty then set default user image, if not set image based on UID
                imageuriobserver()
            }
        }
    }

    //Observes imageURI, if its empty then set default user image, if not set image based on UID
    private fun imageuriobserver(){
        // to imageURI so it can be observed.
        checkforexistingimage(loggedInViewModel.liveFirebaseUser.value!!.uid,"jpg")

        FirebaseStorageManager.imageUri.observe(this,{result ->
            if(result == Uri.EMPTY){
                navHeaderBinding.imageViewLoggedIn.setImageResource(R.drawable.user)
            }else{
                Glide.with(this).load(result.toString()).into(navHeaderBinding.imageViewLoggedIn)
            }
        })
        navHeaderBinding.imageViewLoggedIn.setOnClickListener{
            showImagePicker(intentLauncher)
        }
    }

    fun signOut() {

        val menu: Menu = navView.getMenu()
        val menuItem = menu.findItem(R.id.nav_sign_out)
        menuItem.setOnMenuItemClickListener {
            loggedInViewModel.logOut()
            //Launch Login activity and clear the back stack to stop navigating back to the Home activity
            val intent = Intent(this, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            this.finish()
            //startActivity(Intent(this, home::class.java))
            true
        }

    }

    fun changeTheme(){
        val menu: Menu = navView.getMenu()
        val menuItem = menu.findItem(R.id.weatherTheme)
        menuItem.setOnMenuItemClickListener {
            if(getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                println("NIGHT MODE OFF")
            }else{
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                println("NIGHT MODE ON")
            }
            true
        }

    }

    private fun registerImagePickerCallback() {

        intentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {

                            FirebaseStorageManager.updateUserImage(loggedInViewModel.liveFirebaseUser.value!!.uid,
                                readImageUri(result.resultCode,result.data),navView.imageViewLoggedIn,true
                            )
                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        //Toast.makeText(applicationContext,homeBinding.navView.id.toString(),Toast.LENGTH_SHORT).show()
    }

}