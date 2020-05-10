package com.vvash17company.weatherapp.models.apiservicemodels

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") var value: Int
)