package com.vvash17company.weatherapp

import android.os.Bundle
import android.widget.LinearLayout.HORIZONTAL
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vvash17company.weatherapp.models.City
import com.vvash17company.weatherapp.services.CitiesApiService
import com.vvash17company.weatherapp.services.OpenWeatherApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val retrofitCities = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.cities_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitOpenWeather = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.owa_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val citiesService = retrofitCities.create(CitiesApiService::class.java)
        val openWeatherApiService = retrofitOpenWeather.create(OpenWeatherApiService::class.java)

        citiesService.getCities().enqueue(object : Callback<List<City>> {
            override fun onResponse(
                call: Call<List<City>>,
                response: Response<List<City>>
            ) {
                if (response.isSuccessful) {


                }
            }

            override fun onFailure(call: Call<List<City>>, t: Throwable) {
//                progressBar.visibility = RecyclerView.GONE
            }
        })


    }


}
