package org.wit.myweather.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


    @Parcelize
    data class WeatherModel(var id: Long = 0,
                            var Country: String = "",
                            var County: String = "",
                            var City: String = "",
                            var Temperature: String = "",

                            var WebLocationID : String = "",
                            var WebTemperatureClass : String = "") : Parcelable
