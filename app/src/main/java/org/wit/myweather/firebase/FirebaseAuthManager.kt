package org.wit.myweather.firebase

import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.wit.myweather.R
import org.wit.myweather.activities.home
import org.wit.myweather.ui.auth.Login
import java.lang.Exception

class FirebaseAuthManager(application: Application, login: Login?) {

    val AUTH_CODE = 400
    lateinit var listener: FirebaseAuth.AuthStateListener
    lateinit var providers: List<AuthUI.IdpConfig>

    private var application: Application? = null
    private var loginInstance: Login? = null

    var firebaseAuth: FirebaseAuth? = null
    var firebaseUI: AuthUI? = null
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    var loggedOut = MutableLiveData<Boolean>()

    init {
        providers()
        this.application = application
        this.loginInstance = login
        firebaseUI = AuthUI.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        if (firebaseAuth!!.currentUser != null) {
            liveFirebaseUser.postValue(firebaseAuth!!.currentUser)
            loggedOut.postValue(false)
        }
    }

    fun providers(){
        providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.AnonymousBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
        )
    }

    fun login(){

        listener = object:FirebaseAuth.AuthStateListener{
            override fun onAuthStateChanged(p0: FirebaseAuth) {
                val user = p0.currentUser
                if(user != null){ //If authenticated
                    loginInstance?.startActivity(Intent(application, home::class.java))
                }else{ //Not authenticated
                    try {
                        loginInstance?.startActivityForResult(
                            AuthUI.getInstance().createSignInIntentBuilder().setTheme(R.style.UItheme).setLogo(R.drawable.user)
                                .setAvailableProviders(providers).build(),
                            AUTH_CODE
                        )
                    }catch (e: Exception){
                        println("Error occurred " +e.toString())
                    }
                    }
            }

        }
        firebaseAuth?.addAuthStateListener(listener)
    }

    fun logOut() {
        firebaseAuth!!.signOut()
        firebaseUI!!.signOut(application!!.applicationContext)
        loggedOut.postValue(true)
    }
}