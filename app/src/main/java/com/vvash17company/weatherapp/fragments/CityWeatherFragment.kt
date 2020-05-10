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
import com.vvash17company.weatherapp.models.CityWeatherData
import com.vvash17company.weatherapp.models.additional.DayAndNightData
import com.vvash17company.weatherapp.models.additional.HumidityData
import com.vvash17company.weatherapp.models.additional.PrecipidationData
import com.vvash17company.weatherapp.models.additional.WindSpeedData
import java.util.*


class CityWeatherFragment : Fragment() {

    private lateinit var currentWeatherData: CityWeatherData
    private lateinit var cityName: TextView
    private lateinit var time: TextView
    private lateinit var degrees: TextView
    private lateinit var perceived: TextView
    private lateinit var currentWeatherLayout: ConstraintLayout

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentWeatherData = Gson().fromJson(
            arguments?.getString("cityWeatherData"),
            CityWeatherData::class.java
        )

        var view = inflater.inflate(R.layout.weather_layout, container, false)
        cityName = view.findViewById(R.id.cityName)
        time = view.findViewById(R.id.time)
        degrees = view.findViewById(R.id.degrees)
        perceived = view.findViewById(R.id.perceived)
        currentWeatherLayout = view.findViewById(R.id.currentWeatherLayout)

        cityName.text = currentWeatherData.mainWeatherData.cityName
        val currentDate = Date(currentWeatherData.mainWeatherData.unixTime)


        val dayDate = Date(currentWeatherData.additionWeatherData.dayAndNight.dayUnixTime)
        val nightDate = Date(currentWeatherData.additionWeatherData.dayAndNight.nightUnixTime)

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
            currentWeatherData.mainWeatherData.realDegree.minus(273.15)
        degrees.text = "%.1f".format(degreesCelsius) + "\t℃"
        val perceivedCelsius: Double? =
            currentWeatherData.mainWeatherData.perceivedDegree.minus(273.15)
        perceived.text = "Perceived : " + "%.1f".format(perceivedCelsius) + "\t℃"

        initializePrecipidationLayout(view, currentWeatherData.additionWeatherData.precipidation)
        initializeHumidityLayout(view, currentWeatherData.additionWeatherData.humidity)
        initializeWindSpeedLayout(view, currentWeatherData.additionWeatherData.windSpeed)
        initializeDayAndNightLayout(view, currentWeatherData.additionWeatherData.dayAndNight)

        return view
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
        fun newInstance(cityWeatherData: CityWeatherData): CityWeatherFragment {
            val fragment =
                CityWeatherFragment()
            val args = Bundle()
            args.putString("cityWeatherData", Gson().toJson(cityWeatherData))
            fragment.arguments = args
            return fragment
        }
    }

}
