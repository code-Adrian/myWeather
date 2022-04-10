package org.wit.myweather.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize


    @Parcelize
    data class WeatherModel(var id: Long = 0,
                            var Country: String = "",
                            var County: String = "",
                            var City: String = "",
                            var Temperature: String = "0",
                            var TemperatureLow: String = "0",
                            var WebLink: String = "",
                            var Image: Int = 0,
                            var Type: String = "API",
                            var Favourite: Boolean = false)           : Parcelable

    {
        @Exclude
        fun map() : Map<String,Any?> {
            return mapOf<String, Any?>(
                "id" to id,
                "country" to Country,
                "county" to County,
                "city" to City,
                "temperature" to Temperature,
                "temperatureLow" to TemperatureLow,
                "webLink" to WebLink,
                "image" to Image,
                "type" to Type,
                "favourite" to Favourite
            )
        }
    }



