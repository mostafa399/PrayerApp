package com.mostafahelal.prayerapp.prayer.data.remote.response

import com.google.gson.annotations.SerializedName

data class CalendarFormatResponse(
    @SerializedName("date") val date: String? = null,
    @SerializedName("month") val monthResponse: MonthResponse? = null,
    @SerializedName("year") val year: String? = null,
    @SerializedName("format") val format: String? = null,
    @SerializedName("weekday") val weekdayResponse: WeekdayResponse? = null,
    @SerializedName("day") val day: String? = null,
    @SerializedName("holidays") val holidays: List<String>? = null,
)


data class WeekdayResponse(
    @SerializedName("en") val en: String? = null,
    @SerializedName("ar") val ar: String? = null
)

data class MonthResponse(
    @SerializedName("number") val number: Int? = null,
    @SerializedName("en") val en: String? = null,
    @SerializedName("ar") val ar: String? = null
)
