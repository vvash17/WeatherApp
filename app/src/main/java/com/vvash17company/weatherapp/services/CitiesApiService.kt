package com.vvash17company.weatherapp.services

import com.vvash17company.weatherapp.models.City
import retrofit2.Call
import retrofit2.http.GET

interface CitiesApiService {
    @GET("rest/v2/all?fields=name")
    fun getCities(): Call<List<City>>
}