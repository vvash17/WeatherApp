package com.vvash17company.weatherapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.gson.Gson
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
                        var weatherData: WeatherData = Gson().fromJson("{\"coord\": { \"lon\": 139,\"lat\": 35},\n" +
                                "  \"weather\": [\n" +
                                "    {\n" +
                                "      \"id\": 800,\n" +
                                "      \"main\": \"Clear\",\n" +
                                "      \"description\": \"clear sky\",\n" +
                                "      \"icon\": \"01n\"\n" +
                                "    }\n" +
                                "  ],\n" +
                                "  \"base\": \"stations\",\n" +
                                "  \"main\": {\n" +
                                "    \"temp\": 281.52,\n" +
                                "    \"feels_like\": 278.99,\n" +
                                "    \"temp_min\": 280.15,\n" +
                                "    \"temp_max\": 283.71,\n" +
                                "    \"pressure\": 1016,\n" +
                                "    \"humidity\": 93\n" +
                                "  },\n" +
                                "  \"wind\": {\n" +
                                "    \"speed\": 0.47,\n" +
                                "    \"deg\": 107.538\n" +
                                "  },\n" +
                                "  \"clouds\": {\n" +
                                "    \"all\": 2\n" +
                                "  },\n" +
                                "  \"dt\": 1560350192,\n" +
                                "  \"sys\": {\n" +
                                "    \"type\": 3,\n" +
                                "    \"id\": 2019346,\n" +
                                "    \"message\": 0.0065,\n" +
                                "    \"country\": \"JP\",\n" +
                                "    \"sunrise\": 1560281377,\n" +
                                "    \"sunset\": 1560333478\n" +
                                "  },\n" +
                                "  \"timezone\": 32400,\n" +
                                "  \"id\": 1851632,\n" +
                                "  \"name\": \"Shuzenji\",\n" +
                                "  \"cod\": 200\n" +
                                "}",WeatherData::class.java)
                        var forecastsData: ForecastsData = Gson().fromJson("{\n" +
                                "  \"cod\": \"200\",\n" +
                                "  \"message\": 0,\n" +
                                "  \"cnt\": 40,\n" +
                                "  \"list\": [\n" +
                                "    {\n" +
                                "      \"dt\": 1578409200,\n" +
                                "      \"main\": {\n" +
                                "        \"temp\": 284.92,\n" +
                                "        \"feels_like\": 281.38,\n" +
                                "        \"temp_min\": 283.58,\n" +
                                "        \"temp_max\": 284.92,\n" +
                                "        \"pressure\": 1020,\n" +
                                "        \"sea_level\": 1020,\n" +
                                "        \"grnd_level\": 1016,\n" +
                                "        \"humidity\": 90,\n" +
                                "        \"temp_kf\": 1.34\n" +
                                "      },\n" +
                                "      \"weather\": [\n" +
                                "        {\n" +
                                "          \"id\": 804,\n" +
                                "          \"main\": \"Clouds\",\n" +
                                "          \"description\": \"overcast clouds\",\n" +
                                "          \"icon\": \"04d\"\n" +
                                "        }\n" +
                                "      ],\n" +
                                "      \"clouds\": {\n" +
                                "        \"all\": 100\n" +
                                "      },\n" +
                                "      \"wind\": {\n" +
                                "        \"speed\": 5.19,\n" +
                                "        \"deg\": 211\n" +
                                "      },\n" +
                                "      \"sys\": {\n" +
                                "        \"pod\": \"d\"\n" +
                                "      },\n" +
                                "      \"dt_txt\": \"2020-01-07 15:00:00\"\n" +
                                "    }\n" +
                                "],\n" +
                                "\"city\": {\n" +
                                "    \"id\": 2643743,\n" +
                                "    \"name\": \"London\",\n" +
                                "    \"coord\": {\n" +
                                "      \"lat\": 51.5073,\n" +
                                "      \"lon\": -0.1277\n" +
                                "    },\n" +
                                "    \"country\": \"GB\",\n" +
                                "    \"timezone\": 0,\n" +
                                "    \"sunrise\": 1578384285,\n" +
                                "    \"sunset\": 1578413272\n" +
                                "  }\n" +
                                "}",ForecastsData::class.java)
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
                        cityWeatherAdapter.notifyDataSetChanged()
                    }
                }
            }


            override fun onFailure(call: Call<List<City>>, t: Throwable) {

            }


        }
        )


        //TODO realy implementation
//
//        //main caller func for cities
//        citiesService.getCities().enqueue(object : Callback<List<City>> {
//            override fun onResponse(
//                call: Call<List<City>>,
//                response: Response<List<City>>
//            ) {
//                if (response.isSuccessful) {
//                    var cities: List<City> = response.body()!!
//                    var cityWeatherDatas: ArrayList<CityWeatherData> = ArrayList()
//                    cityWeatherAdapter =
//                        CityWeatherAdapter(supportFragmentManager, 0, cityWeatherDatas)
//                    viewPager.adapter = cityWeatherAdapter
//                    for (city: City in cities) {
//                        //main caller func for currentWeather
//                        openWeatherApiService.getCurrentForecast(
//                            city.name,
//                            resources.getString(R.string.api_key)
//                        )
//                            .enqueue(object : Callback<WeatherData> {
//                                override fun onResponse(
//                                    call: Call<WeatherData>,
//                                    response: Response<WeatherData>
//                                ) {
//                                    var weatherData: WeatherData? = response.body()
//                                    if (weatherData != null) {
//                                        //main caller func for forecast
//                                        openWeatherApiService.getFutureForecasts(
//                                            city.name,
//                                            resources.getString(R.string.api_key)
//                                        ).enqueue(object : Callback<ForecastsData> {
//                                            override fun onResponse(
//                                                call: Call<ForecastsData>,
//                                                response: Response<ForecastsData>
//                                            ) {
//                                                var forecastsData: ForecastsData = response.body()!!
//                                                cityWeatherDatas.add(
//                                                    CityWeatherData(
//                                                        city,
//                                                        MainWeatherData(
//                                                            city.name,
//                                                            weatherData.unixTime,
//                                                            weatherData.weatherDetails.realDegree,
//                                                            weatherData.weatherDetails.perceivedDegree,
//                                                            ""
//                                                        ),
//                                                        AdditionalWeatherData(
//                                                            PrecipidationData(
//                                                                "Precipidation",
//                                                                "",
//                                                                100
//                                                            ),
//                                                            HumidityData("Humidity", "", 100),
//                                                            WindSpeedData("Wind Speed", "", 0.54),
//                                                            DayAndNightData(
//                                                                "Day and Night",
//                                                                "",
//                                                                0,
//                                                                0
//                                                            )
//                                                        ),
//                                                        forecastsData
//                                                    )
//                                                )
//                                                cityWeatherAdapter.notifyDataSetChanged()
//                                            }
//
//                                            override fun onFailure(
//                                                call: Call<ForecastsData>,
//                                                t: Throwable
//                                            ) {
//                                                var a = "";
//                                            }
//
//
//                                        })
//
//                                    }
//                                }
//
//                                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
//
//                                }
//
//                            })
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<City>>, t: Throwable) {
////                progressBar.visibility = RecyclerView.GONE
//            }
//        })


    }


}
