package com.dastan.weatherfinal.data.repository

import com.dastan.weatherfinal.domain.model.Units
import com.dastan.weatherfinal.domain.model.Weather

interface WeatherRepository {
    suspend fun getWeather(city: String, units: Units): Weather
}
