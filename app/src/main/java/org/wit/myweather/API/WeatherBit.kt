package org.wit.myweather.API

import android.os.StrictMode
import android.util.Log
import org.json.JSONObject
import org.wit.myweather.R
import java.lang.Exception
import java.net.URL

const val key = "9d48c9db73324e459ecd0305406ddb15"

//Query and response for 16 day weather forcast
fun returnQuery(Country: String,County: String,City: String) : String? {
allowNetwork()

var query = ("https://api.weatherbit.io/v2.0/forecast/daily?country=" + Country + "&" + "county=" + County+"&" + "city=" + City + "&" + "key="+key)
println(query)
var response:String?
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
    println(query)
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
            tempMax = main.getString("max_temp")

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
            tempMin = main.getString("low_temp")

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
            tempMin = main.getString("max_temp")
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
            tempMin = main.getString("low_temp")
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