package com.vvash17company.weatherapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.vvash17company.weatherapp.models.CityWeatherData

class CityWeatherAdapter(fm: FragmentManager, behavior: Int, cityWeathers: List<CityWeatherData>) :
    FragmentStatePagerAdapter(
        fm,
        behavior
    ) {
    private var cityWeathers: List<CityWeatherData> = cityWeathers

    override fun getItem(position: Int): Fragment {
        var cityWeatherData : CityWeatherData = cityWeathers[position]
        var currentFrag
    }

    override fun getCount(): Int {
        return cityWeathers.size
    }

}