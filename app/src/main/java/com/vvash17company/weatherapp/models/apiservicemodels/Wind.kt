package com.vvash17company.weatherapp.models.apiservicemodels

import com.google.gson.annotations.SerializedName

class Wind(
    @SerializedName("speed") var speed: Long
)