package com.vvash17company.weatherapp.models.apiservicemodels

import com.vvash17company.weatherapp.models.City

data class CityWeatherData(
    var city: City,
    var currentWeather: WeatherData,
    var forecasts: ForecastsData
)