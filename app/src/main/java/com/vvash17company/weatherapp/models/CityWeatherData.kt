package com.vvash17company.weatherapp.models

import com.vvash17company.weatherapp.models.forecast.ForecastWeatherData
import com.vvash17company.weatherapp.models.main.MainWeatherData

data class CityWeatherData(
    val city: City,
    val mainWeatherData: MainWeatherData,
    val additionWeatherData: AdditionalWeatherData,
    val forecastWeatherData: ForecastWeatherData
)