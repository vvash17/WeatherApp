package com.vvash17company.weatherapp.models.additional

data class DayAndNightData(
    var name: String,
    var iconId: String,
    var dayUnixTime: Long,
    var nightUnixTime: Long
)