package org.wit.myweather.webscraper

import android.os.StrictMode
import android.widget.Toast
import org.jsoup.Jsoup
import org.wit.myweather.R
import java.lang.Exception
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread
import kotlin.time.days


fun getLocationByWebLink(weblink : String) : String{
    allowNetwork()
    var location = ""
    try{
        val getWeather = Jsoup.connect(weblink).ignoreContentType(true).get()
        val weather = getWeather.getElementById("wob_loc").text()
location = weather
    }catch (e: Exception){

    }
    return location
}



fun getPeakTemp(country: String, county: String,city:String) : String {
    allowNetwork()
    var temperature = ""
    try {

        if (county.equals("")) {
            val getWeather =
                    Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + city + "")
                            .ignoreContentType(true).get()
            val weather =
                    (getWeather.getElementsByClass("vk_bk TylWce").get(0).getElementsByClass("wob_t q8U8x")
                            .get(0).text())
            temperature = weather.replace("°C", "")
        } else {

            val getWeather =
                    Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + county.replace(" ", "+") + "+" + city + "")
                            .ignoreContentType(true).get()

            val weather =
                  (getWeather.getElementsByClass("vk_bk TylWce").get(0).getElementsByClass("wob_t q8U8x")
                            .get(0).text())
            temperature = weather.replace("°C", "")
        }

    } catch (e: Exception) {
        temperature = "1000"
    }

    return temperature

}

fun getLowestTemp(country: String, county: String,city:String) : String{
    allowNetwork()
    var temperature = ""
    try {
        if (county.equals("")) {
            val getWeather =
                Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + city + "")
                    .ignoreContentType(true).get()
            val weather =
                (getWeather.getElementsByClass("QrNVmd ZXCv8e").get(0).getElementsByClass("wob_t")
                    .get(0).text())
            temperature = weather.replace("°C", "")
        } else {

            val getWeather =
                Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + county.replace(" ", "+") + "+" + city + "")
                    .ignoreContentType(true).get()
            val weather =
                (getWeather.getElementsByClass("QrNVmd ZXCv8e").get(0).getElementsByClass("wob_t")
                    .get(0).text())
            temperature = weather.replace("°C", "")
        }
    }catch (e:Exception){temperature = "1000"}
    return temperature

}

fun getWeatherStatusByWebLink(weblink: String) : String{
var weatherStatus = ""


        val location =  getLocationByWebLink(weblink).split(",")
        val getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + location[0] + "+" + location[1] + "").ignoreContentType(true).get()
        val status = getWeather.getElementById("wob_dc").text()
weatherStatus = status

    return weatherStatus
}


fun getWeatherStatus(country: String, county: String,city:String) : String{
    var weatherStatus = ""

try {
    if (county.equals("")) {
        val getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + city + "").ignoreContentType(true).get()
        val status = getWeather.getElementById("wob_dc").text()
        weatherStatus = status
    } else {
        val getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + county.replace(" ", "+") + "+" + city + "")
                .ignoreContentType(true).get()
        val status = getWeather.getElementById("wob_dc").text()
        weatherStatus = status
    }
}catch (e:Exception){
}


    return weatherStatus
}


fun setImage(country: String, county: String,city:String,weblink: String) : Int{
    var image : Int = 0

if(weblink.equals("")){

    val status = getWeatherStatus(country,county,city)
    if(status.equals("Rain") || status.equals("Scattered showers") || status.equals("Rain and snow") || status.equals("Scattered thunderstorms") || status.equals("Showers")){
        image = R.drawable.rain
    }
    if(status.equals("Cloudy")){
        image = R.drawable.verycloudy
    }
    if(status.equals("Mostly cloudy") || status.equals("Partly cloudy") || status.equals("Mostly sunny")){
        image = R.drawable.cloudysunny
    }
    if(status.equals("Sunny") || status.equals("Clear") || status.equals("Clear with periodic clouds")) {
        image = R.drawable.sun
    }
    if(status.equals("Snow")){
        image = R.drawable.snowflake
    }
}else {

    val status = getWeatherStatusByWebLink(weblink)
    if(status.equals("Rain") || status.equals("Scattered showers") || status.equals("Rain and snow") || status.equals("Scattered thunderstorms") || status.equals("Showers")){
        image = R.drawable.rain
    }
    if(status.equals("Cloudy")){
        image = R.drawable.verycloudy
    }
    if(status.equals("Mostly cloudy") || status.equals("Partly cloudy") || status.equals("Mostly sunny")){
        image = R.drawable.cloudysunny
    }
    if(status.equals("Sunny") || status.equals("Clear") || status.equals("Clear with periodic clouds")){
        image = R.drawable.sun
    }
    if(status.equals("Snow")){
        image = R.drawable.snowflake
    }
}
    return image
}


fun getLocation() : String{
    allowNetwork()
    var Location = ""

    val getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast").ignoreContentType(true).get()
    val location = getWeather.getElementById("wob_loc").text()

    if(location.contains(",")){
       val loc = location.split(",")
        if(loc.size <= 1){

            Location = loc[1].replace("County ","")
        }else if(loc.size == 2){

            Location = loc[1].replace("County ","")
        }else if(loc.size == 3){

            Location = loc[1].replace("County ","")
        }
    }
    return Location
}

fun getDateDay() : String{

    val day = LocalDate.now().dayOfWeek.name.toLowerCase()

    return day.capitalize()
}

fun getPeakTemp() : String{
    val getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast").ignoreContentType(true).get()
    val temp = getWeather.getElementById("wob_tm").text()

    return temp+"°C"
}

fun getWeatherStatus() : Int {

    var image : Int = 0

    val getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast").ignoreContentType(true).get()
    val status = getWeather.getElementById("wob_dc").text()

        if(status.equals("Rain") || status.equals("Scattered showers") || status.equals("Rain and snow") || status.equals("Scattered thunderstorms") || status.equals("Showers")){
            image = R.drawable.rain
        }
        if(status.equals("Cloudy")){
            image = R.drawable.verycloudy
        }
        if(status.equals("Mostly cloudy") || status.equals("Partly cloudy") || status.equals("Mostly sunny")){
            image = R.drawable.cloudysunny
        }
        if(status.equals("Sunny") || status.equals("Clear") || status.equals("Clear with periodic clouds")){
            image = R.drawable.sun
        }
    if(status.equals("Snow")){
        image = R.drawable.snowflake
    }

    return image
}

fun getWeeklyPeakTemp(country: String, county: String,city:String) : ArrayList<String>{

    val list : ArrayList<String> = ArrayList()
    try {
        if (county.equals("")) {
            val getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + city + "").ignoreContentType(true).get()
            for(i in 0..7){
                var weekly = (getWeather.getElementsByClass("gNCp2e").get(i).getElementsByClass("wob_t")
                        .get(0).text())
                list.add(weekly+"°C")
            }
        } else {
            val getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + county.replace(" ", "+") + "+" + city + "")
                    .ignoreContentType(true).get()
            for(i in 0..7){
                var weekly = (getWeather.getElementsByClass("gNCp2e").get(i).getElementsByClass("wob_t")
                        .get(0).text())
                list.add(weekly+"°C")
            }

        }
    }catch (e:Exception){
        println(e)
    }

    return list
}

fun getWeeklylowTemp(country: String, county: String,city:String) : ArrayList<String>{

    val list : ArrayList<String> = ArrayList()
    try {
        if (county.equals("")) {
            val getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + city + "").ignoreContentType(true).get()
            for(i in 0..7){
                val weekly = (getWeather.getElementsByClass("QrNVmd ZXCv8e").get(i).getElementsByClass("wob_t")
                        .get(0).text())
                list.add(weekly+"°C")

            }
        } else {
            val getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + county.replace(" ", "+") + "+" + city + "")
                    .ignoreContentType(true).get()
            for(i in 0..7){
                val weekly = (getWeather.getElementsByClass("QrNVmd ZXCv8e").get(i).getElementsByClass("wob_t")
                        .get(0).text())
                list.add(weekly+"°C")

            }

        }
    }catch (e:Exception){
        println(e)
    }

    return list
}

fun getWeekDays(country: String, county: String,city:String) : ArrayList<String>{

    val list : ArrayList<String> = ArrayList()
    try {
        if (county.equals("")) {
            val getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + city + "").ignoreContentType(true).get()
            for(i in 0..7){
                val weekly = (getWeather.getElementsByClass("Z1VzSb").get(i).text())
                list.add(weekly)
            }
        } else {
            val getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + county.replace(" ", "+") + "+" + city + "")
                    .ignoreContentType(true).get()
            for(i in 0..7){
                val weekly = (getWeather.getElementsByClass("Z1VzSb").get(i).text())
                list.add(weekly)
            }

        }
    }catch (e:Exception){
        println(e)
    }

    return list
}

fun getWeeklyWeather(country: String, county: String,city:String) : ArrayList<Int>{

    val list : ArrayList<Int> = ArrayList()
    try {
        if (county.equals("")) {
            val getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + city + "").ignoreContentType(true).get()
            for(i in 0..7){
                val status = (getWeather.getElementsByClass("gNCp2e").get(i).getElementsByClass("wob_t")
                        .get(0).text())

                if(status.toInt() >= 18){
                    list.add(R.drawable.sun)
                }else
                if(status.toInt() >= 14){
                    list.add(R.drawable.cloudysunny)
                }else
                if(status.toInt() >= 3){
                    list.add(R.drawable.verycloudy)
                }else
                if(status.toInt() <= 1){
                    list.add(R.drawable.snowflake)
                }



            }
        } else {
            val getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + county.replace(" ", "+") + "+" + city + "")
                    .ignoreContentType(true).get()
            for(i in 0..7) {
                val status = (getWeather.getElementsByClass("gNCp2e").get(i).getElementsByClass("wob_t")
                        .get(0).text())

                if (status.toInt() <= 1) {
                    list.add(R.drawable.snowflake)
                }
                if (status.toInt() >= 2) {
                    list.add(R.drawable.verycloudy)
                }
                if (status.toInt() >= 14) {
                    list.add(R.drawable.cloudysunny)
                }
                if(status.toInt() >= 18){
                    list.add(R.drawable.sun)
                }
            }
        }
    }catch (e:Exception){
        println(e)
    }

    return list
}

private fun allowNetwork(){
    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
}