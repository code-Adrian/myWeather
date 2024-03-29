package org.wit.myweather.API

import android.os.StrictMode
import android.util.Log
import org.json.JSONObject
import org.wit.myweather.R
import org.wit.myweather.models.WeatherModel
import java.lang.Exception
import java.net.URL
val key = "a4195b06c78a4ac0984eace150dd30d5"

val keylist = arrayListOf<String>(
    "a4195b06c78a4ac0984eace150dd30d5"
    ,"302e71680cec4f13b1f8005144371e18",
    "9d48c9db73324e459ecd0305406ddb15",
     "7303ce1106774f04b7f0a8ab138c6b98")

fun ranKey():String{
    return keylist.random().toString()
}

//Query and response for 16 day weather forcast
fun returnQuery(Country: String,County: String,City: String) : String? {
allowNetwork()
var query = ("https://api.weatherbit.io/v2.0/forecast/daily?country=" + Country + "&" + "county=" + County+"&" + "city=" + City + "&" + "key="+ key)
var response:String?
println(query)
try{
    response = URL(query).readText(Charsets.UTF_8)
}catch(e: Exception){
    println(e.toString())
    response = null
}
    return response
}

fun returnQueryCoordinated(Latitude: String, Longitude: String) : String? {
    allowNetwork()
    var query = ("https://api.weatherbit.io/v2.0/forecast/daily?lat=" + Latitude + "&" + "lon=" + Longitude+"&" + "key="+key)

    var response:String?
    try{
        response = URL(query).readText(Charsets.UTF_8)
    }catch(e: Exception){
        println(e.toString())
        response = null
    }
    return response
}


fun getPeakCoordinated(Latitude: String, Longitude: String) : String{
    allowNetwork()
    var tempMax = ""
    try{
        val jsonObject = JSONObject(returnQueryCoordinated(Latitude,Longitude))
        val main = jsonObject.getJSONArray("data").getJSONObject(0)
        tempMax = main.getString("max_temp")

    }catch (e:Exception){
        print(e.toString())

    }
    return tempMax
}
fun setIconCoordinated(Latitude: String, Longitude: String) : Int {
    allowNetwork()

    var image = 0

    try{

        val jsonObject = JSONObject(returnQueryCoordinated(Latitude,Longitude))

            val main = jsonObject.getJSONArray("data").getJSONObject(0).getJSONObject("weather")
            val icon = main.getString("icon")
            val resId = R.drawable::class.java.getId(icon)
            image = (resId)

    }catch (e:Exception){
        print(e.toString())

    }
    return image
}




fun getPeak(Country: String,County: String,City: String) : String{
    allowNetwork()
    var tempMax = ""
    try{
            val jsonObject = JSONObject(returnQuery(Country, County, City))
            val main = jsonObject.getJSONArray("data").getJSONObject(0)
            tempMax = main.getString("max_temp").replace("°C","")

    }catch (e:Exception){
        print(e.toString())

    }
    return tempMax
}

fun getLow(Country: String,County: String,City: String) : String{
    allowNetwork()
    var tempMin = ""
    try{
            val jsonObject = JSONObject(returnQuery(Country, County, City))
            val main = jsonObject.getJSONArray("data").getJSONObject(0)
            tempMin = main.getString("low_temp").replace("°C","")

    }catch (e:Exception){
        print(e.toString())

    }
    return tempMin
}


fun getWeeklyPeak(Country: String,County: String,City: String) : ArrayList<String>{
    allowNetwork()

    var tempMin = ""

    var list : ArrayList<String> = ArrayList()

    try{

            val jsonObject = JSONObject(returnQuery(Country, County, City))

        for (i in 0..7) {
            val main = jsonObject.getJSONArray("data").getJSONObject(i)
            tempMin = main.getString("max_temp").replace("°C","")
            list.add(tempMin)
        }

    }catch (e:Exception){
        print(e.toString())

    }
    return list
}


fun getWeeklyLow(Country: String,County: String,City: String) : ArrayList<String>{
    allowNetwork()

    var tempMin = ""

    var list : ArrayList<String> = ArrayList()

    try{

            val jsonObject = JSONObject(returnQuery(Country, County, City))
        for (i in 0..7) {
            val main = jsonObject.getJSONArray("data").getJSONObject(i)
            tempMin = main.getString("low_temp").replace("°C","")
            list.add(tempMin)
        }

    }catch (e:Exception){
    print(e.toString())

    }
    return list
}



fun setIcon(Country: String,County: String,City: String) : ArrayList<Int> {
    allowNetwork()

    var imagelist :  ArrayList<Int> = ArrayList()

    try{

            val jsonObject = JSONObject(returnQuery(Country, County, City))
        for(i in 0..7) {
            val main = jsonObject.getJSONArray("data").getJSONObject(i).getJSONObject("weather")
            val icon = main.getString("icon")
            val resId = R.drawable::class.java.getId(icon)
            imagelist.add(resId)
        }
    }catch (e:Exception){
        print(e.toString())

    }
    return imagelist
}

fun getIconNameList(Country: String,County: String,City: String) : ArrayList<String> {
    allowNetwork()

    var imagelist :  ArrayList<String> = ArrayList()
    try{

        val jsonObject = JSONObject(returnQuery(Country, County, City))
        for(i in 0..7) {
            val main = jsonObject.getJSONArray("data").getJSONObject(i).getJSONObject("weather")
            val icon = main.getString("icon")
            imagelist.add(icon)
        }
    }catch (e:Exception){
        print(e.toString())

    }
    return imagelist
}

fun getWeatherByLatLon(lat: String,lon:String) : WeatherModel{
    var model = WeatherModel()
    try{
        val jsonObject = JSONObject(returnQueryCoordinated(lat,lon))
        val main = jsonObject.getJSONArray("data").getJSONObject(0)
        val iconmain = jsonObject.getJSONArray("data").getJSONObject(0).getJSONObject("weather")
        val tempMax = main.getString("max_temp")
        val tempMin = main.getString("low_temp")
        val icon = iconmain.getString("icon")
        val resId = R.drawable::class.java.getId(icon)
        model.Temperature = tempMax.replace("°C","")
        model.TemperatureLow = tempMin.replace("°C","")
        model.Image = resId
    }catch (e:Exception){
        print(e.toString())

    }
    return model
}


inline fun <reified T: Class<*>> T.getId(resourceName: String): Int {
    return try {
        val idField = getDeclaredField (resourceName)
        idField.getInt(idField)
    } catch (e:Exception) {
        e.printStackTrace()
        -1
    }
}


private fun allowNetwork(){
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
}