package org.wit.myweather.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.models.WeatherStore
import org.wit.myweather.models.WeatherTemperatureModel

var Id = 0L
val uniqueFirebaseID: String = (android.os.Build.MODEL.toString() + " " + android.os.Build.ID+ " " + android.os.Build.USER + " --WeatherModel").replace(".","")
internal fun ID():Long{
    return Id++;
}

object FirebaseDBManager: WeatherStore {

    val weather = ArrayList<WeatherModel>()


    override fun getAll(weathers: MutableLiveData<MutableList<WeatherModel>>, firebaseUser: FirebaseUser?) {
        val localweather = ArrayList<WeatherModel>()
        var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child(firebaseUser!!.uid)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {

                for (p0: DataSnapshot in p0.children) {

                    if (p0 != null) {

                        val country = (p0.child("country").getValue().toString())

                        val county = (p0.child("county").getValue().toString())
                        val city = (p0.child("city").getValue().toString())
                        val temperature = (p0.child("temperature").getValue().toString())
                        val temperaturelow = (p0.child("temperatureLow").getValue().toString())
                        val weblink = (p0.child("webLink").getValue().toString())
                        val id = (p0.child("id").getValue().toString().toLong())
                        val image = (p0.child("image").getValue().toString().toInt())
                        val type = (p0.child("type").getValue().toString())
                        val favourite = (p0.child("favourite").getValue().toString().toBoolean())

                        val weatherModel = WeatherModel(id, country, county, city, temperature,temperaturelow, weblink,image,type,favourite)
                        Id+=id

                        localweather.add(weatherModel)

                        weathers.value = localweather
                    }
                }
            }
        })

    }


    override fun getAllWeatherTemperature(weathers: MutableLiveData<MutableList<WeatherTemperatureModel>>, firebaseUser: FirebaseUser?) {
        val localweather = ArrayList<WeatherTemperatureModel>()
        var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child(firebaseUser!!.uid+"WeatherTemperatureCards")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {

                for (p0: DataSnapshot in p0.children) {

                    if (p0 != null) {

                        val id = (p0.child("id").getValue().toString()).toLong()

                        val weekDay = p0.child("weekDay").getValue() as ArrayList<String>
                        val minimumTemperature = p0.child("minimumTemperature").getValue() as ArrayList<String>
                        val peakTemperature = p0.child("peakTemperature").getValue() as ArrayList<String>
                        val imageName = p0.child("imageName").getValue() as ArrayList<String>

                        val weatherTempModel = WeatherTemperatureModel(id,weekDay,peakTemperature,minimumTemperature,imageName)
                        Id+=id

                        localweather.add(weatherTempModel)


                    }
                }
                weathers.value = localweather
            }
        })

    }

    override fun getSpecificWeatherTemperature(modelid: String, weathers: MutableLiveData<MutableList<WeatherTemperatureModel>>, firebaseUser: FirebaseUser?) {
        val localweather = ArrayList<WeatherTemperatureModel>()
        var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child(firebaseUser!!.uid+"WeatherTemperatureCards")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {

                for (p0: DataSnapshot in p0.children) {

                    if (p0 != null) {

                        val id = (p0.child("id").getValue().toString()).toLong()

                        val weekDay = p0.child("weekDay").getValue() as ArrayList<String>
                        val minimumTemperature = p0.child("minimumTemperature").getValue() as ArrayList<String>
                        val peakTemperature = p0.child("peakTemperature").getValue() as ArrayList<String>
                        val imageName = p0.child("imageName").getValue() as ArrayList<String>

                        val weatherTempModel = WeatherTemperatureModel(id,weekDay,peakTemperature,minimumTemperature,imageName)
                        if(id.toString().equals(modelid)) {
                            localweather.add(weatherTempModel)
                        }

                    }
                }
                weathers.value = localweather
            }
        })
    }

    override fun getAllFavourite(weathers: MutableLiveData<MutableList<WeatherModel>>, firebaseUser: FirebaseUser?) {
        val localweather = ArrayList<WeatherModel>()
        var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child(firebaseUser!!.uid)

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {

                for (p0: DataSnapshot in p0.children) {

                    if (p0 != null) {

                        val country = (p0.child("country").getValue().toString())

                        val county = (p0.child("county").getValue().toString())
                        val city = (p0.child("city").getValue().toString())
                        val temperature = (p0.child("temperature").getValue().toString())
                        val temperaturelow = (p0.child("temperatureLow").getValue().toString())
                        val weblink = (p0.child("webLink").getValue().toString())
                        val id = (p0.child("id").getValue().toString().toLong())
                        val image = (p0.child("image").getValue().toString().toInt())
                        val type = (p0.child("type").getValue().toString())
                        val favourite = (p0.child("favourite").getValue().toString().toBoolean())

                        val weatherModel = WeatherModel(id, country, county, city, temperature,temperaturelow, weblink,image,type,favourite)
                        Id+=id
                        //Filtering
                        if(favourite == true) {
                            localweather.add(weatherModel)
                        }

                    }
                }
                weathers.value = localweather
            }
        })
    }

    //var ref = FirebaseDatabase.getInstance("https://myweather-95318-default-rtdb.firebaseio.com/").getReference().child(uniqueFirebaseID)

    override fun localgetAll(): MutableList<WeatherModel> {
        return weather
    }



    override fun create(weather: WeatherModel,firebaseUser: MutableLiveData<FirebaseUser>) {
        var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child(firebaseUser.value!!.uid)
       // weather.id = ID()+rand(500,1000000000)
        database.child(weather.id.toString()).setValue(weather)
    }

    override fun createWeatherTemperature(weather: WeatherTemperatureModel, firebaseUser: MutableLiveData<FirebaseUser>) {
        var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child(firebaseUser.value!!.uid+"WeatherTemperatureCards")
        database.child(weather.id.toString()).setValue(weather)
    }

    override fun delete(weather: WeatherModel,firebaseUser: MutableLiveData<FirebaseUser>) {
        var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child(firebaseUser.value!!.uid)
        database.child(weather.id.toString()).removeValue().addOnSuccessListener {
            println(weather.id.toString() + " removed successfuly!")
        }.addOnFailureListener{
            println(weather.id.toString() + " failed to remove...?")
        }
    }

    override fun deleteWeatherTemperature(weather: WeatherTemperatureModel, firebaseUser: MutableLiveData<FirebaseUser>) {
        var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child(firebaseUser.value!!.uid+"WeatherTemperatureCards")
        database.child(weather.id.toString()).removeValue().addOnSuccessListener {
            println(weather.id.toString() + " removed successfuly!")
        }.addOnFailureListener{
            println(weather.id.toString() + " failed to remove...?")
        }
    }
    override fun updateWeatherTemperature(weather: WeatherTemperatureModel, firebaseUser: MutableLiveData<FirebaseUser>) {
        var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child(firebaseUser.value!!.uid+"WeatherTemperatureCards")
        database.child(weather.id.toString()).updateChildren(weather.map()).addOnSuccessListener {
            println(weather.id.toString() + " updated successfuly!")
        }.addOnFailureListener{
            println(weather.id.toString() + " failed to update...?")
        }
    }
    override fun update(weather: WeatherModel,firebaseUser: MutableLiveData<FirebaseUser>) {
        var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child(firebaseUser.value!!.uid)
       database.child(weather.id.toString()).updateChildren(weather.map()).addOnSuccessListener {
           println(weather.id.toString() + " updated successfuly!")
       }.addOnFailureListener{
           println(weather.id.toString() + " failed to update...?")
       }
    }



    private fun cloudSave(localweather: ArrayList<WeatherModel>){

    var ref = FirebaseDatabase.getInstance("https://myweather-95318-default-rtdb.firebaseio.com/").getReference().child(uniqueFirebaseID).
    ref.setValue(localweather)
}

    fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Error, Integer required" }
        return (start..end).random()
    }
}