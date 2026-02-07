package com.dastan.weatherfinal.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dastan.weatherfinal.data.firebase.Favorite
import com.dastan.weatherfinal.data.repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val favorites: List<Favorite> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class FavoritesViewModel(private val repo: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private val _state = MutableStateFlow(FavoritesUiState())
    val state: StateFlow<FavoritesUiState> = _state.asStateFlow()
    private var currentUid: String? = null

    init {
        initAuth()
    }
    private fun initAuth() = viewModelScope.launch {
        try {
            currentUid = repo.ensureAuthenticated()
            repo.getFavorites(currentUid!!).collectLatest { list ->
                _state.value = FavoritesUiState(favorites = list, isLoading = false)
            }
        } catch (e: Exception) {
            _state.value = FavoritesUiState(isLoading = false, error = e.message)
        }
    }
    fun add(city: String, note: String) = viewModelScope.launch {
        currentUid?.let { repo.addFavorite(it, city, note) }
    }
    fun update(id: String, city: String, note: String) = viewModelScope.launch {
        currentUid?.let { repo.updateFavorite(it, id, city, note) }
    }

    fun delete(id: String) = viewModelScope.launch {
        currentUid?.let { repo.deleteFavorite(it, id) }
    }
}
