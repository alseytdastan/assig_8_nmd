package com.dastan.weatherfinal.data.remote.api

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

object ApiClient {

    private val json = Json { ignoreUnknownKeys = true }
    private val converter = json.asConverterFactory("application/json".toMediaType())

    private val forecastRetrofit = Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/")
        .addConverterFactory(converter)
        .build()

    private val geocodingRetrofit = Retrofit.Builder()
        .baseUrl("https://geocoding-api.open-meteo.com/")
        .addConverterFactory(converter)
        .build()

    val forecastApi: OpenMeteoApi = forecastRetrofit.create(OpenMeteoApi::class.java)
    val geocodingApi: OpenMeteoApi = geocodingRetrofit.create(OpenMeteoApi::class.java)
}
