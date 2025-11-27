package com.cavies.pokedex.presentation.ui.favorite

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.usecase.GetFavoritesUseCase
import com.cavies.pokedex.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavorites: GetFavoritesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase
) : ViewModel() {
    var uiState by mutableStateOf(FavoriteUiState())
        private set

    fun loadFavoritePokemons() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val favoritePokemons = getFavorites()
                uiState = uiState.copy(
                    favoritePokemons = favoritePokemons,
                    isLoading = false
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.message ?: "Error al cargar Pokémon",
                    isLoading = false
                )
            }
        }
    }

    fun onFavoriteClick(pokemon: Pokemon) {
        viewModelScope.launch {
            toggleFavorite(pokemon)
            loadFavoritePokemons()
        }
    }
}