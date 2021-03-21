package org.wit.myweather.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class WeatherModel {
    @Parcelize
    data class WeatherModel(var id: Long = 0,var Country: String = "", var County: String = "", var City: String = "" ) : Parcelable
}