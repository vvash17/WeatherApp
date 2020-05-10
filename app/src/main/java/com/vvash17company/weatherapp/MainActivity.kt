package com.vvash17company.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.vvash17company.weatherapp.adapters.CityWeatherAdapter
import com.vvash17company.weatherapp.models.City
import com.vvash17company.weatherapp.services.CitiesApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var cityWeatherAdapter: CityWeatherAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.mainPager)

        val retrofitCities = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.cities_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val citiesService = retrofitCities.create(CitiesApiService::class.java)

        citiesService.getCities().enqueue(object : Callback<List<City>> {
            override fun onResponse(
                call: Call<List<City>>,
                response: Response<List<City>>
            ) {
                if (response.isSuccessful) {
                    var cities: List<City> = response.body()!!
                    cityWeatherAdapter = CityWeatherAdapter(supportFragmentManager, 0, cities)
                    viewPager.adapter = cityWeatherAdapter
                }
            }

            override fun onFailure(call: Call<List<City>>, t: Throwable) {

            }
        })


    }


}
