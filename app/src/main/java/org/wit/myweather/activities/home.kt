package org.wit.myweather.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseUser
import org.wit.myweather.R
import org.wit.myweather.databinding.HomeBinding
import org.wit.myweather.databinding.NavHeaderBinding
import org.wit.myweather.ui.auth.LoggedInViewModel
import org.wit.myweather.ui.auth.Login
import kotlin.math.sign

class home : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding: HomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loggedInViewModel : LoggedInViewModel
    private lateinit var navHeaderBinding : NavHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)


        val navView = homeBinding.navView
        navView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.menuFragment, R.id.weatherListFragment,R.id.weatherFragment,R.id.weatherTemperatureFragment,R.id.weatherEditFragment), drawerLayout)
            setupActionBarWithNavController(navController, appBarConfiguration)

        signOut(navView)
    }

    override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this).get(LoggedInViewModel::class.java)

        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null) {
                val currentUser = loggedInViewModel.liveFirebaseUser.value
                if (currentUser != null) updateNavHeader(loggedInViewModel.liveFirebaseUser.value!!)

            }
        })

        loggedInViewModel.loggedOut.observe(this, Observer { loggedout ->
            if (loggedout) {
                //startActivity(Intent(this, Login::class.java))
                println(loggedout)
            }
        })
    }

    private fun updateNavHeader(currentUser: FirebaseUser) {
        println(loggedInViewModel.liveFirebaseUser.value!!.uid)
        var headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
        if(currentUser.isAnonymous){
            navHeaderBinding.emailText.text = "Anonymous"
        }
        if
        (currentUser.isEmailVerified){
            navHeaderBinding.emailText.text = currentUser.email
        }
        if(!currentUser.phoneNumber.isNullOrEmpty()){
            navHeaderBinding.emailText.text = currentUser.phoneNumber
        }
        if(!currentUser.email.isNullOrEmpty()){
            navHeaderBinding.emailText.text = currentUser.email
        }
        if(!currentUser.displayName.isNullOrEmpty()){
            navHeaderBinding.emailText.text = currentUser.displayName
        }



            println(currentUser.photoUrl)

    }

    fun signOut(navView: NavigationView) {

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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        //Toast.makeText(applicationContext,homeBinding.navView.id.toString(),Toast.LENGTH_SHORT).show()

    }




}