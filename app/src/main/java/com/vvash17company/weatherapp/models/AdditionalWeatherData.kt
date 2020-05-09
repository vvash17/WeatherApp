package com.vvash17company.weatherapp.models

import com.vvash17company.weatherapp.models.additional.DayAndNightData
import com.vvash17company.weatherapp.models.additional.HumidityData
import com.vvash17company.weatherapp.models.additional.PrecipidationData
import com.vvash17company.weatherapp.models.additional.WindSpeedData

data class AdditionalWeatherData(
    var precipidation: PrecipidationData,
    var humidity: HumidityData,
    var windSpeed: WindSpeedData,
    var dayAndNight: DayAndNightData
)