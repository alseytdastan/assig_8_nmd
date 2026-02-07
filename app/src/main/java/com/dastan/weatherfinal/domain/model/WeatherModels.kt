package com.dastan.weatherfinal.domain.model

data class Weather(
    val city: String,
    val temp: Double,
    val humidity: Int,
    val wind: Double,
    val condition: String,
    val lastUpdate: String,
    val forecast: List<ForecastDay>
)

data class ForecastDay(
    val date: String,
    val min: Double,
    val max: Double,
    val condition: String
)

enum class Units { C, F }
