package org.wit.myweather.webscraper

import android.os.StrictMode
import android.widget.Toast
import org.jsoup.Jsoup
import org.wit.myweather.R
import java.lang.Exception
import kotlin.concurrent.thread


fun getLocationByWebLink(weblink : String) : String{
    allowNetwork()
    var location = ""
    try{
        var getWeather = Jsoup.connect(weblink).ignoreContentType(true).get()
        var weather = getWeather.getElementById("wob_loc").text()
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
            var getWeather =
                    Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + city + "")
                            .ignoreContentType(true).get()
            var weather =
                    (getWeather.getElementsByClass("vk_gy gNCp2e").get(0).getElementsByClass("wob_t")
                            .get(0).text())
            temperature = weather.replace("°C", "")
        } else {

            var getWeather =
                    Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + county.replace(" ", "+") + "+" + city + "")
                            .ignoreContentType(true).get()
            println("https://www.google.com/search?q=weather+forecast+" + country + "+" + county.replace(" ", "+") + "+" + city + "")
            var weather =
                    (getWeather.getElementsByClass("vk_gy gNCp2e").get(0).getElementsByClass("wob_t")
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
            var getWeather =
                Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + city + "")
                    .ignoreContentType(true).get()
            var weather =
                (getWeather.getElementsByClass("QrNVmd ZXCv8e").get(0).getElementsByClass("wob_t")
                    .get(0).text())
            temperature = weather.replace("°C", "")
        } else {

            var getWeather =
                Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + county.replace(" ", "+") + "+" + city + "")
                    .ignoreContentType(true).get()
            var weather =
                (getWeather.getElementsByClass("QrNVmd ZXCv8e").get(0).getElementsByClass("wob_t")
                    .get(0).text())
            temperature = weather.replace("°C", "")
        }
    }catch (e:Exception){temperature = "1000"}
    return temperature

}

fun getWeatherStatusByWebLink(weblink: String) : String{
var weatherStatus = ""


        var location =  getLocationByWebLink(weblink).split(",")
        var getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + location[0] + "+" + location[1] + "").ignoreContentType(true).get()
        var status = getWeather.getElementById("wob_dc").text()
weatherStatus = status

    return weatherStatus
}


fun getWeatherStatus(country: String, county: String,city:String) : String{
    var weatherStatus = ""

try {
    if (county.equals("")) {
        var getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + city + "").ignoreContentType(true).get()
        var status = getWeather.getElementById("wob_dc").text()
        weatherStatus = status
    } else {
        var getWeather = Jsoup.connect("https://www.google.com/search?q=weather+forecast+" + country + "+" + county.replace(" ", "+") + "+" + city + "")
                .ignoreContentType(true).get()
        var status = getWeather.getElementById("wob_dc").text()
        weatherStatus = status
    }
}catch (e:Exception){
}


    return weatherStatus
}

fun setImage(country: String, county: String,city:String,weblink: String) : Int{
    var image : Int = 0

if(weblink.equals("")){

    var status = getWeatherStatus(country,county,city)
    if(status.equals("Rain") || status.equals("Scattered showers") || status.equals("Rain and snow") || status.equals("Scattered thunderstorms") || status.equals("Showers")){
        image = R.drawable.rain
    }
    if(status.equals("Cloudy")){
        image = R.drawable.verycloudy
    }
    if(status.equals("Mostly cloudy") || status.equals("Partly cloudy") || status.equals("Mostly sunny")){
        image = R.drawable.cloudysunny
    }
    if(status.equals("Sunny") || status.equals("Clear")){
        image = R.drawable.sun
    }
}else {

    var status = getWeatherStatusByWebLink(weblink)
    if(status.equals("Rain") || status.equals("Scattered showers") || status.equals("Rain and snow") || status.equals("Scattered thunderstorms") || status.equals("Showers")){
        image = R.drawable.rain
    }
    if(status.equals("Cloudy")){
        image = R.drawable.verycloudy
    }
    if(status.equals("Mostly cloudy") || status.equals("Partly cloudy") || status.equals("Mostly sunny")){
        image = R.drawable.cloudysunny
    }
    if(status.equals("Sunny") || status.equals("Clear")){
        image = R.drawable.sun
    }
}
    return image
}


private fun allowNetwork(){
    var policy = StrictMode.ThreadPolicy.Builder().permitAll().build();
    StrictMode.setThreadPolicy(policy);
}