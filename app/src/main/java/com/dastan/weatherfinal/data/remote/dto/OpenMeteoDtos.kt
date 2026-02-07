package com.dastan.weatherfinal.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeocodingResponseDto(
    val results: List<GeocodingResultDto> = emptyList()
)

@Serializable
data class GeocodingResultDto(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String? = null
)

@Serializable
data class ForecastResponseDto(
    val current: CurrentDto,
    val daily: DailyDto
)

@Serializable
data class CurrentDto(
    @SerialName("temperature_2m") val temp: Double,
    @SerialName("relative_humidity_2m") val humidity: Int,
    @SerialName("wind_speed_10m") val wind: Double,
    @SerialName("weather_code") val code: Int,
    val time: String
)

@Serializable
data class DailyDto(
    val time: List<String>,
    @SerialName("temperature_2m_max") val max: List<Double>,
    @SerialName("temperature_2m_min") val min: List<Double>,
    @SerialName("weather_code") val code: List<Int>
)
