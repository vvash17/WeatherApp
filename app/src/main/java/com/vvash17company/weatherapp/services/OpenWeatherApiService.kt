package com.vvash17company.weatherapp.services

import com.vvash17company.weatherapp.models.apiservicemodels.ForecastsData
import com.vvash17company.weatherapp.models.apiservicemodels.WeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {
    @GET("forecast")
    fun getFutureForecasts(@Query("q") city: String, @Query("appid") apiKey: String): Call<ForecastsData>

    @GET("weather")
    fun getCurrentForecast(@Query("q") city: String, @Query("appid") apiKey: String): Call<WeatherData>
}