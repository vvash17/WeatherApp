package com.vvash17company.weatherapp.models.main

data class MainWeatherData(
    var cityName: String,
    var unixTime: Long,
    var realDegree: Double,
    var perceivedDegree: Double,
    var iconId: String
)