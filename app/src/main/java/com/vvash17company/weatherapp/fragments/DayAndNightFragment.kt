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
import com.vvash17company.weatherapp.models.additional.DayAndNightData
import java.text.SimpleDateFormat

class DayAndNightFragment : Fragment() {

    private lateinit var dayAndNightData: DayAndNightData

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dayAndNightData = Gson().fromJson(
            arguments?.getString("dayAndNightData"),
            DayAndNightData::class.java
        )
        var view = inflater.inflate(R.layout.additional_weather_details_layout, container, false)
        var attributeName: TextView = view.findViewById(R.id.attributeName)
        var attributeValue: TextView = view.findViewById(R.id.attributeValue)
        var icon: ImageView = view.findViewById(R.id.iconImage)

        var formatter = SimpleDateFormat("hh:mm a")
        attributeName.text = dayAndNightData.name
        attributeValue.text =
            formatter.format(dayAndNightData.dayUnixTime) + " " + formatter.format(dayAndNightData.nightUnixTime)

        return view
    }

    companion object {
        fun newInstance(
            dayAndNightData: DayAndNightData
        ): DayAndNightFragment {
            val fragment =
                DayAndNightFragment()
            val args = Bundle()
            args.putString("dayAndNightData", Gson().toJson(dayAndNightData))
            fragment.arguments = args
            return fragment
        }
    }
}