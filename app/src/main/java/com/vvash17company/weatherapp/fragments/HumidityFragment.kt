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
import com.vvash17company.weatherapp.models.additional.HumidityData

class HumidityFragment : Fragment() {
    private lateinit var humidityData: HumidityData
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        humidityData = Gson().fromJson(
            arguments?.getString("humidityData"),
            HumidityData::class.java
        )
        var view = inflater.inflate(R.layout.additional_weather_details_layout, container, false)
        var attributeName: TextView = view.findViewById(R.id.attributeName)
        var attributeValue: TextView = view.findViewById(R.id.attributeValue)
        var icon: ImageView = view.findViewById(R.id.iconImage)

        attributeName.text = humidityData.name
        attributeValue.text = humidityData.percentage.toString()+"%"

        return view
    }

    companion object {
        fun newInstance(
            humidityData: HumidityData
        ): HumidityFragment {
            val fragment =
                HumidityFragment()
            val args = Bundle()
            args.putString("humidityData", Gson().toJson(humidityData))
            fragment.arguments = args
            return fragment
        }
    }
}