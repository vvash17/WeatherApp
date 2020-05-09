package com.vvash17company.weatherapp.models.apiservicemodels

import com.google.gson.annotations.SerializedName

data class DayDetails(
    @SerializedName("sunrise") var dayUnixTime: Long,
    @SerializedName("sunset") var nightUnixTime: Long
)