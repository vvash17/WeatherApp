package com.vvash17company.weatherapp.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.vvash17company.weatherapp.R
import com.vvash17company.weatherapp.models.AdditionalWeatherData
import com.vvash17company.weatherapp.models.City
import com.vvash17company.weatherapp.models.additional.DayAndNightData
import com.vvash17company.weatherapp.models.additional.HumidityData
import com.vvash17company.weatherapp.models.additional.PrecipidationData
import com.vvash17company.weatherapp.models.additional.WindSpeedData
import com.vvash17company.weatherapp.models.apiservicemodels.ForecastsData
import com.vvash17company.weatherapp.models.apiservicemodels.WeatherData
import com.vvash17company.weatherapp.models.main.MainWeatherData
import com.vvash17company.weatherapp.services.OpenWeatherApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class CityWeatherFragment : Fragment() {

    private lateinit var city: City


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        city = Gson().fromJson(
            arguments?.getString("city"),
            City::class.java
        )

        var view = inflater.inflate(R.layout.weather_layout, container, false)

        val retrofitOpenWeather = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.owa_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val openWeatherApiService = retrofitOpenWeather.create(OpenWeatherApiService::class.java)

        openWeatherApiService.getCurrentForecast(city.name, resources.getString(R.string.api_key))
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
                        ).enqueue(
                            object : Callback<ForecastsData> {
                                override fun onResponse(
                                    call: Call<ForecastsData>,
                                    response: Response<ForecastsData>
                                ) {
                                    var forecastsData: ForecastsData = response.body()!!

                                    var mainWeatherData = MainWeatherData(
                                        city.name,
                                        weatherData.unixTime,
                                        weatherData.weatherDetails.realDegree,
                                        weatherData.weatherDetails.perceivedDegree,
                                        ""
                                    )
                                    var additionalWeatherData = AdditionalWeatherData(
                                        PrecipidationData("Precipidation", "", 100),
                                        HumidityData(
                                            "Humidity",
                                            "",
                                            weatherData.weatherDetails.humidityPercentage
                                        ),
                                        WindSpeedData("Wind Speed", "", weatherData.wind.speed),
                                        DayAndNightData(
                                            "Day and Night",
                                            "",
                                            weatherData.dayDetails.dayUnixTime,
                                            weatherData.dayDetails.nightUnixTime
                                        )
                                    )
                                    initializeCurrentWeatherLayout(view, mainWeatherData,additionalWeatherData)
                                    initializePrecipidationLayout(
                                        view,
                                        additionalWeatherData.precipidation
                                    )
                                    initializeHumidityLayout(
                                        view,
                                        additionalWeatherData.humidity
                                    )
                                    initializeWindSpeedLayout(
                                        view,
                                        additionalWeatherData.windSpeed
                                    )
                                    initializeDayAndNightLayout(
                                        view,
                                        additionalWeatherData.dayAndNight
                                    )
                                }

                                override fun onFailure(
                                    call: Call<ForecastsData>,
                                    t: Throwable
                                ) {
                                    var a = "";
                                }


                            })
                    }


                }

                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    var a = ""
                }
            }
            )

        return view
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun initializeCurrentWeatherLayout(
        view: View,
        mainWeatherData: MainWeatherData,
        additionalWeatherData: AdditionalWeatherData
    ) {
        val cityName: TextView = view.findViewById(R.id.cityName)
        val time: TextView = view.findViewById(R.id.time)
        val degrees: TextView = view.findViewById(R.id.degrees)
        val perceived: TextView = view.findViewById(R.id.perceived)
        val currentWeatherLayout: ConstraintLayout = view.findViewById(R.id.currentWeatherLayout)

        cityName.text = mainWeatherData.cityName
        val currentDate = Date(mainWeatherData.unixTime)


        val dayDate = Date(additionalWeatherData.dayAndNight.dayUnixTime)
        val nightDate = Date(additionalWeatherData.dayAndNight.nightUnixTime)

        if (currentDate.after(dayDate) and currentDate.before(nightDate)) {
            currentWeatherLayout.setBackgroundColor(Color.rgb(255, 102, 0))
        } else {
            currentWeatherLayout.setBackgroundColor(
                Color.rgb(
                    51, 153, 255
                )
            )
        }

        time.text = SimpleDateFormat("EE DD MMM hh:mm a").format(currentDate)
        val degreesCelsius: Double? =
            mainWeatherData.realDegree.minus(273.15)
        degrees.text = "%.1f".format(degreesCelsius) + "\t℃"
        val perceivedCelsius: Double? =
            mainWeatherData.perceivedDegree.minus(273.15)
        perceived.text = "Perceived " + "%.1f".format(perceivedCelsius) + "\t℃"
    }

    @SuppressLint("SetTextI18n")
    private fun initializePrecipidationLayout(view: View, precipidationData: PrecipidationData) {
        val attributeName: TextView = view.findViewById(R.id.attributeNameP)
        val attributeValue: TextView = view.findViewById(R.id.attributeValueP)
        val icon: ImageView = view.findViewById(R.id.iconImageP)

        attributeName.text = precipidationData.name
        attributeValue.text = precipidationData.percentage.toString() + "%"
    }

    @SuppressLint("SetTextI18n")
    private fun initializeHumidityLayout(view: View, humidityData: HumidityData) {
        val attributeName: TextView = view.findViewById(R.id.attributeNameH)
        val attributeValue: TextView = view.findViewById(R.id.attributeValueH)
        val icon: ImageView = view.findViewById(R.id.iconImageH)

        attributeName.text = humidityData.name
        attributeValue.text = humidityData.percentage.toString() + "%"
    }

    @SuppressLint("SetTextI18n")
    private fun initializeWindSpeedLayout(view: View, windSpeedData: WindSpeedData) {
        val attributeName: TextView = view.findViewById(R.id.attributeNameW)
        val attributeValue: TextView = view.findViewById(R.id.attributeValueW)
        var icon: ImageView = view.findViewById(R.id.iconImageW)

        attributeName.text = windSpeedData.name
        attributeValue.text = windSpeedData.double.toString() + "km/h"
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initializeDayAndNightLayout(view: View, dayAndNightData: DayAndNightData) {
        val attributeName: TextView = view.findViewById(R.id.attributeNameD)
        val attributeValue: TextView = view.findViewById(R.id.attributeValueD)
        val icon: ImageView = view.findViewById(R.id.iconImageD)

        val formatter = java.text.SimpleDateFormat("hh:mm a")
        attributeName.text = dayAndNightData.name
        attributeValue.text =
            formatter.format(dayAndNightData.dayUnixTime) + " " + formatter.format(dayAndNightData.nightUnixTime)
    }

    companion object {
        fun newInstance(city: City): CityWeatherFragment {
            val fragment =
                CityWeatherFragment()
            val args = Bundle()
            args.putString("city", Gson().toJson(city))
            fragment.arguments = args
            return fragment
        }
    }

}
