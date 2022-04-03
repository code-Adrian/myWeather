package org.wit.myweather.ui.auth

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.wit.myweather.activities.home
import org.wit.myweather.firebase.FirebaseAuthManager

class LoginViewModel (app: Application,login: Login) : AndroidViewModel(app)  {


    var firebaseAuthManager : FirebaseAuthManager = FirebaseAuthManager(app,login)
    var liveFirebaseUser : MutableLiveData<FirebaseUser> = firebaseAuthManager.liveFirebaseUser


fun login(){
    firebaseAuthManager.login()
}

}