package com.vvash17company.weatherapp.models.apiservicemodels

import com.google.gson.annotations.SerializedName

data class WeatherData(
    @SerializedName("wind") var wind: Wind,
    @SerializedName("dt") var unixTime: Long,
    @SerializedName("main") var weatherDetails: WeatherDetails,
    @SerializedName("sys") var dayDetails: DayDetails,
    @SerializedName("weather") var weather: List<Weather>,
    @SerializedName("clouds") var precipidation: Clouds,
    @SerializedName("dt_txt")var dateText :String
)