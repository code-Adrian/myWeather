package org.wit.myweather.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import org.wit.myweather.R
import org.wit.myweather.databinding.LoginBinding

class Login: AppCompatActivity() {

    private lateinit var loginViewModel : LoginViewModel
    private lateinit var loginBinding: LoginBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = LoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
    }

    public override fun onStart() {
        super.onStart()
        loginViewModel = ViewModelProvider(this,viewModelFactory { LoginViewModel(this.application,this) }).get(LoginViewModel::class.java)
        listeners()
    }

    private fun listeners(){
        loginBinding.authbutton.setOnClickListener{
            loginViewModel.login()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
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