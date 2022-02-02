package org.wit.myweather.models

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.concurrent.thread
var Id = 0L
val uniqueFirebaseID: String = (android.os.Build.MODEL.toString() + " " + android.os.Build.ID+ " " + android.os.Build.USER + " --WeatherModel").replace(".","")
internal fun ID():Long{
    return Id++;
}

class FireBase_Store: WeatherStore  {

    val weather = ArrayList<WeatherModel>()
    override fun getAll(): MutableList<WeatherModel> {

            return cloudPull()
    }

    override fun create(weathers: WeatherModel) {
        weathers.id = ID()+rand(500,1000000000)
        weather.add(weathers)
        cloudSave()
    }

    override fun delete(weathers: WeatherModel) {
        val foundWeather: WeatherModel? = weather.find { id -> id.id == weathers.id }
        if(foundWeather !=null){
            weather.remove(foundWeather)
        }
        cloudSave()
    }

    override fun update(weathers: WeatherModel) {
 val foundWeather: WeatherModel? = weather.find { id -> id.id == weathers.id }
        if(foundWeather !=null){
            foundWeather.Country = weathers.Country
            foundWeather.County = weathers.County
            foundWeather.City = weathers.City
        }
        println(weathers.id)
        cloudSave()
    }

private fun cloudSave(){
    var ref = FirebaseDatabase.getInstance("https://myweather-95318-default-rtdb.firebaseio.com/").getReference().child(uniqueFirebaseID).
    ref.setValue(weather)
}

private fun cloudPull() : ArrayList<WeatherModel> {

        var ref = FirebaseDatabase.getInstance("https://myweather-95318-default-rtdb.firebaseio.com/").getReference().child(uniqueFirebaseID)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                thread {
                weather.clear()
                for (p0: DataSnapshot in p0.children) {

                    if (p0 != null) {
                        val country = (p0.child("country").getValue().toString())
                        val county = (p0.child("county").getValue().toString())
                        val city = (p0.child("city").getValue().toString())
                        val temperature = (p0.child("temperature").getValue().toString())
                        val weblink = (p0.child("webLink").getValue().toString())
                        val id = (p0.child("id").getValue().toString().toLong())

                        val weatherModel = WeatherModel(id, country, county, city, temperature, weblink)
                        Id+=id
                        weather.add(weatherModel)
                    }
                    }

                }
            }
        })
        return weather
    }


    fun rand(start: Int, end: Int): Int {
        require(start <= end) { "Error, Integer required" }
        return (start..end).random()
    }
}