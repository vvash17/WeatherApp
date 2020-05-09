package com.vvash17company.weatherapp.services

import com.vvash17company.weatherapp.models.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApiService {
    @GET("forecast")
    fun getFutureForecasts(@Query("q") city: String, @Query("appid") apiKey: String): Call<FutureForecastsData>

    @GET("weather")
    fun getCurrentForecast(@Query("q") city: String, @Query("appid") apiKey: String): Call<CurrentWeatherData>
}