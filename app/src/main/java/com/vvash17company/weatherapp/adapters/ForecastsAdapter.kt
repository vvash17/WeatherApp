package com.vvash17company.weatherapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vvash17company.weatherapp.R
import com.vvash17company.weatherapp.models.apiservicemodels.ForecastsData
import com.vvash17company.weatherapp.models.apiservicemodels.WeatherData
import java.text.SimpleDateFormat

class ForecastsAdapter(val forecastsData: ForecastsData) :
    RecyclerView.Adapter<ForecastsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time: TextView = itemView.findViewById(R.id.timeTextView)
        val degrees: TextView = itemView.findViewById(R.id.degreesTextView)
        val icon: ImageView = itemView.findViewById(R.id.forecastImage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastsAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.forecast_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return forecastsData.dataCount
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

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: ForecastsAdapter.ViewHolder, position: Int) {
        val weatherData: WeatherData = forecastsData.forecasts[position]
        val formatter = SimpleDateFormat("dd/MM hh:mm a")

        holder.time.text = formatter.format(weatherData.unixTime * 1000)
        holder.degrees.text =
            "%.1f".format(weatherData.weatherDetails.realDegree.minus(273.15)) + "\t℃"
        setIconImage(holder.icon, weatherData.weather[0].iconId)
    }
}