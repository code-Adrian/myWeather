package org.wit.myweather.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.mainmenu_activity.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.myweather.R
import org.wit.myweather.main.Main

class MenuActivity : AppCompatActivity(){
    lateinit var app : Main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = application as Main
        supportActionBar?.hide();
        setContentView(R.layout.mainmenu_activity)
onMenuButtonClick()


    }

    fun onMenuButtonClick(){
        menu_button.setOnClickListener {
            startActivityForResult(intentFor<WeatherListActivity>(),0)
        }

    }

}