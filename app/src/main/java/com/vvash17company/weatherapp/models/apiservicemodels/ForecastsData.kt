package com.vvash17company.weatherapp.models.apiservicemodels

import com.google.gson.annotations.SerializedName

class ForecastsData(
    @SerializedName("cnt") var dataCount: Int,
    @SerializedName("list") var forecasts: List<WeatherData>
)