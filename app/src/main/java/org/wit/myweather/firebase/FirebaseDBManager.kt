package org.wit.myweather.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.wit.myweather.models.WeatherModel
import org.wit.myweather.models.WeatherStore

var Id = 0L
val uniqueFirebaseID: String = (android.os.Build.MODEL.toString() + " " + android.os.Build.ID+ " " + android.os.Build.USER + " --WeatherModel").replace(".","")
internal fun ID():Long{
    return Id++;
}

object FirebaseDBManager: WeatherStore {

    val weather = ArrayList<WeatherModel>()


    override fun getAll(weathers: MutableLiveData<MutableList<WeatherModel>>,
                        firebaseUser: FirebaseUser?
    ) {
        val localweather = ArrayList<WeatherModel>()
        //var ref = FirebaseDatabase.getInstance("https://myweather-95318-default-rtdb.firebaseio.com/").getReference().child(uniqueFirebaseID)
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

    override fun getAllFavourite(weathers: MutableLiveData<MutableList<WeatherModel>>, firebaseUser: FirebaseUser?) {
        val localweather = ArrayList<WeatherModel>()
        //var ref = FirebaseDatabase.getInstance("https://myweather-95318-default-rtdb.firebaseio.com/").getReference().child(uniqueFirebaseID)
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
                        weathers.value = localweather
                    }
                }
            }
        })
    }

    //var ref = FirebaseDatabase.getInstance("https://myweather-95318-default-rtdb.firebaseio.com/").getReference().child(uniqueFirebaseID)

    override fun localgetAll(): MutableList<WeatherModel> {
        return weather
    }

    private fun map(weather: WeatherModel) : Map<String,Any?>{
        val map = mapOf<String,Any?>(
            "id" to weather.id,
            "country" to weather.Country,
            "county" to weather.County,
            "city" to weather.City,
            "temperature" to weather.Temperature,
            "temperatureLow" to weather.TemperatureLow,
            "webLink" to weather.WebLink,
            "image" to weather.Image,
            "type" to weather.Type,
            "favourite" to weather.Favourite        )

        return map
    }

    override fun create(weather: WeatherModel,firebaseUser: MutableLiveData<FirebaseUser>) {
        var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child(firebaseUser.value!!.uid)
        weather.id = ID()+rand(500,1000000000)
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

    override fun update(weather: WeatherModel,firebaseUser: MutableLiveData<FirebaseUser>) {
        var database: DatabaseReference = FirebaseDatabase.getInstance().reference.child(firebaseUser.value!!.uid)
       database.child(weather.id.toString()).updateChildren(map(weather)).addOnSuccessListener {
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