package org.wit.myweather.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class WeatherTemperatureModel(
                                     var WeekDay: String = "",
                                     var PeakTemperature: Int = 0,
                                     var MinimumTemperature: Int = 0,
                                     var Image: Int = 0
                                                                )           : Parcelable



