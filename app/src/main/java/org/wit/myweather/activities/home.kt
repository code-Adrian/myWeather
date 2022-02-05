package org.wit.myweather.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import org.wit.myweather.R
import org.wit.myweather.databinding.HomeBinding

class home : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding: HomeBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

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
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
        //Toast.makeText(applicationContext,homeBinding.navView.id.toString(),Toast.LENGTH_SHORT).show()

    }
}