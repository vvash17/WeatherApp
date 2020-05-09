package com.vvash17company.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.vvash17company.weatherapp.models.CityWeatherData


class CityWeatherFragment : Fragment() {

    private var currentWeatherData: CityWeatherData? = null
    private var cityName: TextView? = null
    private var time: TextView? = null
    private var degrees: TextView? = null
    private var perceived: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currentWeatherData = Gson().fromJson(
                savedInstanceState?.getString("cityWeatherData"),
                CityWeatherData::class.java
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.weather_layout, container, false)
        cityName = view.findViewById(R.id.cityName)
        time = view.findViewById(R.id.time)
        degrees = view.findViewById(R.id.degrees)
        perceived = view.findViewById(R.id.perceived)

        cityName?.text = currentWeatherData?.mainWeatherData?.cityName
        time?.text = currentWeatherData?.mainWeatherData?.unixTime.toString()
        degrees?.text = currentWeatherData?.mainWeatherData?.realDegree.toString()
        perceived?.text = currentWeatherData?.mainWeatherData?.perceivedDegree.toString()
        //TODO shevseba gaakete
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(cityWeatherData: CityWeatherData) =
            CityWeatherFragment().apply {
                arguments = Bundle().apply {
                    putString("cityWeatherData", Gson().toJson(cityWeatherData))
                }
            }
    }
}
