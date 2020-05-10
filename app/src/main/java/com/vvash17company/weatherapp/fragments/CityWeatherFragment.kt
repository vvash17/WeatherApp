package com.vvash17company.weatherapp.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.vvash17company.weatherapp.R
import com.vvash17company.weatherapp.models.CityWeatherData
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

        fragmentManager?.beginTransaction()?.add(
            R.id.precipidationFrame,
            PrecipitationFragment.newInstance(currentWeatherData.additionWeatherData.precipidation),
            "precipidationFragment"
        )?.add(
            R.id.humidityFrame,
            HumidityFragment.newInstance(currentWeatherData.additionWeatherData.humidity),
            "humidityFragment"
        )?.add(
            R.id.windSpeedFrame,
            WindSpeedFragment.newInstance(currentWeatherData.additionWeatherData.windSpeed),
            "windSpeedFragment"
        )?.add(
            R.id.dayAndNightFrame,
            DayAndNightFragment.newInstance(currentWeatherData.additionWeatherData.dayAndNight),
            "dayAndNightFragment"
        )?.commit()
        return view
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
