package com.vvash17company.weatherapp.models.apiservicemodels

import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName("icon") var iconId: String
)