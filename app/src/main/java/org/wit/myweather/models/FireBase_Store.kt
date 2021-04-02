package org.wit.myweather.models

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

var Id = 0L
val uniqueFirebaseID: String = (android.os.Build.MODEL.toString() + " " + android.os.Build.ID+ " " + android.os.Build.USER + " --WeatherModel").replace(".","")

internal fun ID():Long{
    return Id++;
}

class FireBase_Store: WeatherStore {

    val weather = ArrayList<WeatherModel>()

    override fun getAll(): MutableList<WeatherModel> {
        cloudPull()
        return weather
    }

    override fun create(weathers: WeatherModel) {
        weathers.id = ID()
        weather.add(weathers)
        cloudSave()
    }

    override fun delete(weathers: WeatherModel) {

    }

    override fun update(weathers: WeatherModel) {
 val foundWeather: WeatherModel? = weather.find { id -> id.id == weathers.id }
        if(foundWeather !=null){
            foundWeather.Country = weathers.Country
            foundWeather.County = weathers.County
            foundWeather.City = weathers.City
cloudSave()
        }
    }

private fun cloudSave(){
    var ref = FirebaseDatabase.getInstance("https://weather-18a10-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child(uniqueFirebaseID)
ref.setValue(weather)

}

  fun cloudPull(){
        var ref = FirebaseDatabase.getInstance("https://weather-18a10-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child(uniqueFirebaseID)
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                weather.clear()
                for (p0: DataSnapshot in p0.children) {

                    if (p0 != null) {
                        val country = (p0.child("country").getValue().toString())
                        val county = (p0.child("county").getValue().toString())
                        val city = (p0.child("city").getValue().toString())
                        val temperature = (p0.child("temperature").getValue().toString())
                        val weblink =  (p0.child("webLink").getValue().toString())
                       val id = (p0.child("id").getValue().toString().toLong())

                        val weatherModel = WeatherModel(id,country,county,city,temperature,weblink)

                        weather.add(weatherModel)

                    }

                }
            }
        })
    }

}