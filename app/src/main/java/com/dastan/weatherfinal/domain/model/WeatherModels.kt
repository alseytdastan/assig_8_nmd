package com.dastan.weatherfinal.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Weather(
    val city: String,
    val temp: Double,
    val humidity: Int,
    val wind: Double,
    val condition: String,
    val lastUpdate: String,
    val forecast: List<ForecastDay>
)

@Serializable
data class ForecastDay(
    val date: String,
    val min: Double,
    val max: Double,
    val condition: String
)

@Serializable
enum class Units { C, F }