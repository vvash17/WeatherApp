package com.vvash17company.weatherapp.models.apiservicemodels

import com.google.gson.annotations.SerializedName

class WeatherDetails(
    @SerializedName("temp") var realDegree: Double,
    @SerializedName("feels_like") var perceivedDegree: Double,
    @SerializedName("humidity") var humidityPercentage: Long
)