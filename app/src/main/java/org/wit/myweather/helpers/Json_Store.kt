package org.wit.myweather.models

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.myweather.helpers.*
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "weather.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java,Parse())
    .create()
val listType: Type = object : TypeToken<ArrayList<WeatherModel>>() {}.type


class Json_Store(private val context: Context) : LocalWeatherStore {

    var weathers = mutableListOf<WeatherModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()

        }
    }

    override fun getAll() : MutableList<WeatherModel>{
        //Toast.makeText(context,weathers.size.toString(),Toast.LENGTH_LONG).show()
        weathers.clear()
        deserialize()
        return weathers
    }

   override fun serialize(weather: MutableList<WeatherModel>) {
        val jsonString = gsonBuilder.toJson(weather, listType)

        write(context, JSON_FILE, jsonString)
    }

    override fun deserialize() {
        val jsonString = read(context, JSON_FILE)
         weathers = gsonBuilder.fromJson(jsonString, listType)

    }

}

class Parse : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return JsonPrimitive(src.toString())
    }
}