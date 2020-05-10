package com.vvash17company.weatherapp.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Color.rgb
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.vvash17company.weatherapp.R
import com.vvash17company.weatherapp.adapters.ForecastsAdapter
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
import kotlin.math.absoluteValue


class CityWeatherFragment : Fragment() {

    private lateinit var city: City
    private var isDay: Boolean = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.weather_layout, container, false)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        var errorMessage: TextView = view.findViewById(R.id.errorMessage)

        city = Gson().fromJson(
            arguments?.getString("city"),
            City::class.java
        )
        val retrofitOpenWeather = Retrofit.Builder()
            .baseUrl(resources.getString(R.string.owa_base_url))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val openWeatherApiService = retrofitOpenWeather.create(OpenWeatherApiService::class.java)
        openWeatherApiService.getCurrentForecast(city.name, resources.getString(R.string.api_key))
            .enqueue(object : Callback<WeatherData> {
                @SuppressLint("SetTextI18n")
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
                                    //TODO forecast impl
                                    var forecastsData: ForecastsData = response.body()!!
                                    progressBar.visibility = View.GONE

                                    var mainWeatherData = MainWeatherData(
                                        city.name,
                                        weatherData.unixTime,
                                        weatherData.weatherDetails.realDegree,
                                        weatherData.weatherDetails.perceivedDegree,
                                        weatherData.weather[0].iconId
                                    )
                                    var additionalWeatherData = AdditionalWeatherData(
                                        PrecipidationData(
                                            "Precipidation",
                                            weatherData.precipidation.value
                                        ),
                                        HumidityData(
                                            "Humidity",
                                            weatherData.weatherDetails.humidityPercentage
                                        ),
                                        WindSpeedData("Wind Speed", weatherData.wind.speed),
                                        DayAndNightData(
                                            "Day and Night",
                                            weatherData.dayDetails.dayUnixTime,
                                            weatherData.dayDetails.nightUnixTime
                                        )
                                    )

                                    initializeCurrentWeatherLayout(
                                        view,
                                        mainWeatherData,
                                        additionalWeatherData
                                    )
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
                                    initializeForecasts(view, forecastsData)
                                }

                                @SuppressLint("SetTextI18n")
                                override fun onFailure(
                                    call: Call<ForecastsData>,
                                    t: Throwable
                                ) {
                                    handleError(progressBar, errorMessage)
                                }
                            })
                    } else {
                        handleError(progressBar, errorMessage)
                    }


                }

                @SuppressLint("SetTextI18n")
                override fun onFailure(call: Call<WeatherData>, t: Throwable) {
                    handleError(progressBar, errorMessage)
                }
            }
            )

        return view
    }

    private fun initializeForecasts(view: View, forecastsData: ForecastsData) {
        val recycler: RecyclerView = view.findViewById(R.id.forecastsRecycler)
        recycler.layoutManager =
            LinearLayoutManager(context, LinearLayout.VERTICAL.absoluteValue, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            recycler.background = resources.getDrawable(R.drawable.border, null)
        }
        val adapter = ForecastsAdapter(forecastsData)
        recycler.adapter = adapter
    }

    @SuppressLint("SetTextI18n")
    private fun handleError(progressBar: ProgressBar, errorMessage: TextView) {
        progressBar.visibility = View.GONE
        errorMessage.text = "Data was not loaded for \n" + city.name
        errorMessage.setBackgroundColor(rgb(220, 20, 60))
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
        val additionalWeatherLayout: ConstraintLayout =
            view.findViewById(R.id.additionalWeatherFragments)
        val iconImage: ImageView = view.findViewById(R.id.weatherIcon)

        setIconImage(iconImage, mainWeatherData.iconId)
        additionalWeatherLayout.background = resources.getDrawable(R.drawable.border, null)

        cityName.text = mainWeatherData.cityName
        val currentDate = Date(mainWeatherData.unixTime)


        val dayDate = Date(additionalWeatherData.dayAndNight.dayUnixTime)
        val nightDate = Date(additionalWeatherData.dayAndNight.nightUnixTime)

        if (currentDate.after(dayDate) and currentDate.before(nightDate)) {
            currentWeatherLayout.setBackgroundColor(Color.rgb(255, 102, 0))
            isDay = true
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

    private fun setIconImage(iconImage: ImageView, iconId: String) {
        when (iconId) {
            "01d" -> iconImage.setBackgroundResource(R.drawable.owa01d)
            "01n" -> iconImage.setBackgroundResource(R.drawable.owa01n)
            "02d" -> iconImage.setBackgroundResource(R.drawable.owa02d)
            "02n" -> iconImage.setBackgroundResource(R.drawable.owa02n)
            "03d" -> iconImage.setBackgroundResource(R.drawable.owa03d)
            "03n" -> iconImage.setBackgroundResource(R.drawable.owa03n)
            "04d" -> iconImage.setBackgroundResource(R.drawable.owa04d)
            "04n" -> iconImage.setBackgroundResource(R.drawable.owa04n)
            "09d" -> iconImage.setBackgroundResource(R.drawable.owa09d)
            "09n" -> iconImage.setBackgroundResource(R.drawable.owa09n)
            "10d" -> iconImage.setBackgroundResource(R.drawable.owa10d)
            "10n" -> iconImage.setBackgroundResource(R.drawable.owa10n)
            "11d" -> iconImage.setBackgroundResource(R.drawable.owa11d)
            "11n" -> iconImage.setBackgroundResource(R.drawable.owa11n)
            "13d" -> iconImage.setBackgroundResource(R.drawable.owa13d)
            "13n" -> iconImage.setBackgroundResource(R.drawable.owa13n)
            "50d" -> iconImage.setBackgroundResource(R.drawable.owa50d)
            "50n" -> iconImage.setBackgroundResource(R.drawable.owa50n)
            else -> iconImage.setBackgroundResource(R.drawable.owa01n)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initializePrecipidationLayout(view: View, precipidationData: PrecipidationData) {
        val attributeName: TextView = view.findViewById(R.id.attributeNameP)
        val attributeValue: TextView = view.findViewById(R.id.attributeValueP)
        val icon: ImageView = view.findViewById(R.id.iconImageP)
        if (isDay) {
            icon.setImageResource(R.drawable.drop)
        } else {
            icon.setImageResource(R.drawable.drop_night)
        }

        attributeName.text = precipidationData.name
        attributeValue.text = precipidationData.percentage.toString() + "%"
    }

    @SuppressLint("SetTextI18n")
    private fun initializeHumidityLayout(view: View, humidityData: HumidityData) {
        val attributeName: TextView = view.findViewById(R.id.attributeNameH)
        val attributeValue: TextView = view.findViewById(R.id.attributeValueH)
        val icon: ImageView = view.findViewById(R.id.iconImageH)
        if (isDay) {
            icon.setImageResource(R.drawable.humidity)
        } else {
            icon.setImageResource(R.drawable.humidity_night)
        }
        attributeName.text = humidityData.name
        attributeValue.text = humidityData.percentage.toString() + "%"
    }

    @SuppressLint("SetTextI18n")
    private fun initializeWindSpeedLayout(view: View, windSpeedData: WindSpeedData) {
        val attributeName: TextView = view.findViewById(R.id.attributeNameW)
        val attributeValue: TextView = view.findViewById(R.id.attributeValueW)
        var icon: ImageView = view.findViewById(R.id.iconImageW)
        if (isDay) {
            icon.setImageResource(R.drawable.flag)
        } else {
            icon.setImageResource(R.drawable.flag_night)
        }
        attributeName.text = windSpeedData.name
        attributeValue.text = windSpeedData.double.toString() + "km/h"
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    private fun initializeDayAndNightLayout(view: View, dayAndNightData: DayAndNightData) {
        val attributeName: TextView = view.findViewById(R.id.attributeNameD)
        val attributeValue: TextView = view.findViewById(R.id.attributeValueD)
        val icon: ImageView = view.findViewById(R.id.iconImageD)

        icon.setImageResource(R.drawable.day_night)

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
