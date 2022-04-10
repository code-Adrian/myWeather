package org.wit.myweather.models

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.android.parcel.Parcelize


@Parcelize
data class WeatherTemperatureModel(
    var id: Long = 0,
    var WeekDay: ArrayList<String>,
    var PeakTemperature: ArrayList<String>,
    var MinimumTemperature: ArrayList<String>,
    var ImageName: ArrayList<String>
                                                                )           : Parcelable


{
    @Exclude
    fun map() : Map<String,Any?> {
        return mapOf<String, Any?>(
            "id" to id,
            "weekDay" to WeekDay,
            "peakTemperature" to PeakTemperature,
            "minimumTemperature" to MinimumTemperature,
            "imageName" to ImageName
        )
    }
}


