package com.vvash17company.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.vvash17company.weatherapp.adapters.CityWeatherAdapter
import com.vvash17company.weatherapp.models.AdditionalWeatherData
import com.vvash17company.weatherapp.models.City
import com.vvash17company.weatherapp.models.CityWeatherData
import com.vvash17company.weatherapp.models.additional.DayAndNightData
import com.vvash17company.weatherapp.models.additional.HumidityData
import com.vvash17company.weatherapp.models.additional.PrecipidationData
import com.vvash17company.weatherapp.models.additional.WindSpeedData
import com.vvash17company.weatherapp.models.apiservicemodels.ForecastsData
import com.vvash17company.weatherapp.models.apiservicemodels.WeatherData
import com.vvash17company.weatherapp.models.main.MainWeatherData
import com.vvash17company.weatherapp.services.CitiesApiService
import com.vvash17company.weatherapp.services.OpenWeatherApiService
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

        val retrofitOpenWeather = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.owa_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val citiesService = retrofitCities.create(CitiesApiService::class.java)
        val openWeatherApiService = retrofitOpenWeather.create(OpenWeatherApiService::class.java)

        //main caller func for cities
        citiesService.getCities().enqueue(object : Callback<List<City>> {
            override fun onResponse(
                call: Call<List<City>>,
                response: Response<List<City>>
            ) {
                if (response.isSuccessful) {
                    var cities: List<City> = response.body()!!
                    var cityWeatherDatas: ArrayList<CityWeatherData> = ArrayList()
                    cityWeatherAdapter =
                        CityWeatherAdapter(supportFragmentManager, 0, cityWeatherDatas)
                    viewPager.adapter = cityWeatherAdapter
                    for (city: City in cities) {
                        //main caller func for currentWeather
                        openWeatherApiService.getCurrentForecast(
                            city.name,
                            resources.getString(R.string.api_key)
                        )
                            .enqueue(object : Callback<WeatherData> {
                                override fun onResponse(
                                    call: Call<WeatherData>,
                                    response: Response<WeatherData>
                                ) {
                                    var weatherData: WeatherData? = response.body()
                                    if (weatherData != null) {
                                        //main caller func for forecast
                                        openWeatherApiService.getFutureForecasts(
                                            city.name,
                                            resources.getString(R.string.api_key)
                                        ).enqueue(object : Callback<ForecastsData> {
                                            override fun onResponse(
                                                call: Call<ForecastsData>,
                                                response: Response<ForecastsData>
                                            ) {
                                                var forecastsData: ForecastsData = response.body()!!
                                                cityWeatherDatas.add(
                                                    CityWeatherData(
                                                        city,
                                                        MainWeatherData(
                                                            city.name,
                                                            weatherData.unixTime,
                                                            weatherData.weatherDetails.realDegree,
                                                            weatherData.weatherDetails.perceivedDegree,
                                                            ""
                                                        ),
                                                        AdditionalWeatherData(
                                                            PrecipidationData(
                                                                "Precipidation",
                                                                "",
                                                                100
                                                            ),
                                                            HumidityData("Humidity", "", 100),
                                                            WindSpeedData("Wind Speed", "", 0.54),
                                                            DayAndNightData(
                                                                "Day and Night",
                                                                "",
                                                                0,
                                                                0
                                                            )
                                                        ),
                                                        forecastsData
                                                    )
                                                )
                                            }

                                            override fun onFailure(
                                                call: Call<ForecastsData>,
                                                t: Throwable
                                            ) {

                                            }


                                        })

                                    }
                                }

                                override fun onFailure(call: Call<WeatherData>, t: Throwable) {

                                }

                            })
                    }
                }
            }

            override fun onFailure(call: Call<List<City>>, t: Throwable) {
//                progressBar.visibility = RecyclerView.GONE
            }
        })


    }


}
