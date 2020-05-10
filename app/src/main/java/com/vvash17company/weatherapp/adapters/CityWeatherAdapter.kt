package com.vvash17company.weatherapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.vvash17company.weatherapp.fragments.CityWeatherFragment
import com.vvash17company.weatherapp.models.City

class CityWeatherAdapter(fm: FragmentManager, behavior: Int, cities: List<City>) :
    FragmentStatePagerAdapter(
        fm,
        behavior
    ) {
    private var cities: List<City> = cities

    override fun getItem(position: Int): Fragment {
        var city: City = cities[position]
        return CityWeatherFragment.newInstance(city)
    }

    override fun getCount(): Int {
        return cities.size
    }

}