package com.dastan.weatherfinal.presentation.weather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dastan.weatherfinal.data.local.SettingsDataStore
import com.dastan.weatherfinal.data.local.WeatherCache
import com.dastan.weatherfinal.data.repository.WeatherRepository
import com.dastan.weatherfinal.data.repository.WeatherRepositoryImpl
import com.dastan.weatherfinal.domain.model.Units
import com.dastan.weatherfinal.domain.model.Weather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeatherUiState(
    val query: String = "",
    val units: Units = Units.C,
    val isLoading: Boolean = false,
    val data: Weather? = null,
    val error: String? = null,
    val isOffline: Boolean = false
)

class WeatherViewModel(
    app: Application,
    private val repo: WeatherRepository = WeatherRepositoryImpl()
) : AndroidViewModel(app) {

    private val settings = SettingsDataStore(app)
    private val _state = MutableStateFlow(WeatherUiState())
    val state: StateFlow<WeatherUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settings.unitsFlow.collectLatest { u ->
                _state.update { it.copy(units = u) }
                if (_state.value.data != null) {
                    search()
                }
            }
        }
        _state.update { it.copy(data = WeatherCache.last) }
    }

    fun onQueryChange(newQuery: String) {
        _state.update { it.copy(query = newQuery) }
    }

    fun toggleUnits() {
        val next = if (_state.value.units == Units.C) Units.F else Units.C
        viewModelScope.launch {
            settings.saveUnits(next)
        }
    }
    fun search() = viewModelScope.launch {
        val city = _state.value.query
        if (city.isBlank()) {
            _state.update { it.copy(error = "Please enter a city name") }
            return@launch
        }
        _state.update { it.copy(isLoading = true, error = null, isOffline = false) }

        try {
            val weather = repo.getWeather(city, _state.value.units)
            WeatherCache.last = weather
            _state.update { it.copy(isLoading = false, data = weather, error = null) }
        } catch (e: Exception) {
            val cached = WeatherCache.last
            if (cached != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        data = cached,
                        error = "Network error. Showing cached data.",
                        isOffline = true
                    )
                }
            } else {
                _state.update { 
                    it.copy(
                        isLoading = false, 
                        error = e.message ?: "An unknown error occurred",
                        isOffline = true 
                    ) 
                }
            }
        }
    }
}
