package com.vvash17company.weatherapp.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.vvash17company.weatherapp.R
import com.vvash17company.weatherapp.models.additional.PrecipidationData
import com.vvash17company.weatherapp.models.additional.WindSpeedData

class WindSpeedFragment : Fragment() {
    private lateinit var windSpeedData: WindSpeedData
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        windSpeedData = Gson().fromJson(
            arguments?.getString("windSpeedData"),
            WindSpeedData::class.java
        )

        var view = inflater.inflate(R.layout.additional_weather_details_layout, container, false)
        var attributeName: TextView = view.findViewById(R.id.attributeName)
        var attributeValue: TextView = view.findViewById(R.id.attributeValue)
        var icon: ImageView = view.findViewById(R.id.iconImage)

        attributeName.text = windSpeedData.name
        attributeValue.text = windSpeedData.double.toString()+"km/h"
        return view
    }

    companion object {
        fun newInstance(
            windSpeedData: WindSpeedData
        ): WindSpeedFragment {
            val fragment =
                WindSpeedFragment()
            val args = Bundle()
            args.putString("windSpeedData", Gson().toJson(windSpeedData))
            fragment.arguments = args
            return fragment
        }
    }
}