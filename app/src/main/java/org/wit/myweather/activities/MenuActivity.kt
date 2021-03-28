package org.wit.myweather.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.mainmenu_activity.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.myweather.R

class MenuActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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