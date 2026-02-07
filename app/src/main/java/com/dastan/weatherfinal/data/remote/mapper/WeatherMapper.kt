package com.dastan.weatherfinal.data.remote.mapper

import com.dastan.weatherfinal.data.remote.dto.ForecastResponseDto
import com.dastan.weatherfinal.domain.model.ForecastDay
import com.dastan.weatherfinal.domain.model.Weather

private fun codeToText(code: Int): String = when (code) {
    0 -> "Clear"
    1, 2, 3 -> "Partly cloudy"
    45, 48 -> "Fog"
    51, 53, 55 -> "Drizzle"
    61, 63, 65 -> "Rain"
    71, 73, 75 -> "Snow"
    else -> "Unknown"
}

fun ForecastResponseDto.toWeather(city: String): Weather {
    val days = daily.time.indices.take(3).map { i ->
        ForecastDay(
            date = daily.time[i],
            min = daily.min[i],
            max = daily.max[i],
            condition = codeToText(daily.code[i])
        )
    }

    return Weather(
        city = city,
        temp = current.temp,
        humidity = current.humidity,
        wind = current.wind,
        condition = codeToText(current.code),
        lastUpdate = current.time,
        forecast = days
    )
}
