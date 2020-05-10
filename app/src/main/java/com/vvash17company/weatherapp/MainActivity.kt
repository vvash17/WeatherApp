package com.vvash17company.weatherapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
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

    @SuppressLint("SetTextI18n")
    private fun handleError(progressBar: ProgressBar, errorMessage: TextView, msg: String) {
        progressBar.visibility = View.GONE
        errorMessage.text = msg
        errorMessage.setBackgroundColor(Color.rgb(220, 20, 60))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val progressBar: ProgressBar = findViewById(R.id.mainProgressBar)
        val errorMessage: TextView = findViewById(R.id.mainErrorMessage)

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
                    progressBar.visibility = View.GONE
                    var cities: List<City> = response.body()!!
                    cityWeatherAdapter = CityWeatherAdapter(supportFragmentManager, 0, cities)
                    viewPager.adapter = cityWeatherAdapter
                } else {
                    handleError(progressBar, errorMessage, "Loaded data is empty")
                }
            }

            override fun onFailure(call: Call<List<City>>, t: Throwable) {
                handleError(progressBar, errorMessage, "Network connection problem")
            }
        })


    }


}
