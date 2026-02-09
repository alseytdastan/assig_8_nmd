package com.dastan.weatherfinal.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.dastan.weatherfinal.domain.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.weatherDataStore by preferencesDataStore(name = "weather_cache")

class WeatherDataStore(private val context: Context) {
    private val json = Json { ignoreUnknownKeys = true }
    private val KEY = stringPreferencesKey("last_weather_json")

    val cachedWeather: Flow<Weather?> = context.weatherDataStore.data.map { prefs ->
        prefs[KEY]?.let {
            try { json.decodeFromString<Weather>(it) } catch (e: Exception) { null }
        }
    }
    suspend fun saveWeather(weather: Weather) {
        context.weatherDataStore.edit { prefs ->
            prefs[KEY] = json.encodeToString(weather)
        }
    }
}