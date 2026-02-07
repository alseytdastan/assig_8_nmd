package com.dastan.weatherfinal.data.repository

import com.dastan.weatherfinal.data.remote.api.ApiClient
import com.dastan.weatherfinal.data.remote.mapper.toWeather
import com.dastan.weatherfinal.domain.model.Units
import com.dastan.weatherfinal.domain.model.Weather

class WeatherRepositoryImpl : WeatherRepository {

    override suspend fun getWeather(city: String, units: Units): Weather {
        val trimmed = city.trim()
        require(trimmed.isNotEmpty()) { "City name cannot be empty" }

        // 1. Geocoding to get coordinates
        val geo = ApiClient.geocodingApi.geocode(trimmed)
        val first = geo.results.firstOrNull() ?: throw Exception("City '$trimmed' not found")

        val unitParam = if (units == Units.C) "celsius" else "fahrenheit"

        // 2. Forecast using coordinates
        val dto = ApiClient.forecastApi.forecast(
            lat = first.latitude,
            lon = first.longitude,
            temperatureUnit = unitParam
        )

        // 3. Map to domain model
        return dto.toWeather(first.name)
    }
}
