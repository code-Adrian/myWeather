package org.wit.myweather.API

import android.os.StrictMode
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

fun queryReturn(Lon: String,Lat: String) : String? {
    allowNetwork()
    var query = ("https://api.bigdatacloud.net/data/reverse-geocode-client?latitude="+Lat+"&longitude="+Lon)
    var response:String?

    try{
        response = URL(query).readText(Charsets.UTF_8)
    }catch(e: Exception){
        println(e.toString())
        response = null
    }
    return response
}

fun getCityQuery(Lon: String,Lat: String): String {
    allowNetwork()
        var city = ""
    try {
        val jsonObject = JSONObject(queryReturn(Lon, Lat))
        val main = jsonObject.getJSONObject("localityInfo").getJSONArray("administrative")
        var iterator = 0
        for(i in 0 .. main.length()-1){
            //println(main.getJSONObject(i).getString("name").toString() + main.getJSONObject(i).getString("adminLevel").toString())
            if(main.getJSONObject(i).getString("adminLevel").toString().equals("9")){
                iterator = i
            }
        }
        if(iterator == 0){
            for(i in 0 .. main.length()-1){
                if(main.getJSONObject(i).getString("adminLevel").toString().equals("7")){
                    iterator = i
                }
            }
        }
        city = main.getJSONObject(iterator).getString("name")

    }catch(e: Exception){
        println(e)
    }
    println(city)
    return city
}

fun getRegionQuery(Lon: String,Lat: String): String {
    allowNetwork()
    var region = ""
    try {
        val jsonObject = JSONObject(queryReturn(Lon, Lat))
        val main = jsonObject.getString("principalSubdivision")
        region = main

    }catch(e: Exception){
        println(e)
    }

    return region
}

fun getCountryQuery(Lon: String,Lat: String): String {
    allowNetwork()
    var countryName = ""
    try {
        val jsonObject = JSONObject(queryReturn(Lon, Lat))
        val main = jsonObject.getString("countryName")
        countryName = main

    }catch(e: Exception){
        println(e)
    }

    return countryName
}

 private fun allowNetwork(){
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
}