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

class PrecipitationFragment : Fragment() {

    private lateinit var precipidationData: PrecipidationData

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        precipidationData = Gson().fromJson(
            arguments?.getString("precipidationData"),
            PrecipidationData::class.java
        )
        var view = inflater.inflate(R.layout.additional_weather_details_layout, container, false)
        var attributeName: TextView = view.findViewById(R.id.attributeName)
        var attributeValue: TextView = view.findViewById(R.id.attributeValue)
        var icon: ImageView = view.findViewById(R.id.iconImage)

        attributeName.text = precipidationData.name
        attributeValue.text = precipidationData.percentage.toString()+"%"

        return view
    }

    companion object {
        fun newInstance(
            precipidationData: PrecipidationData
        ): PrecipitationFragment {
            val fragment =
                PrecipitationFragment()
            val args = Bundle()
            args.putString("precipidationData", Gson().toJson(precipidationData))
            fragment.arguments = args
            return fragment
        }
    }
}