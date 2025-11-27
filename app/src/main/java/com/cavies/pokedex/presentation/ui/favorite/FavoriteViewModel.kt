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

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavorites().collect { favorites ->
                uiState = uiState.copy(
                    favoritePokemons = favorites,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    fun onFavoriteClick(pokemon: Pokemon) {
        viewModelScope.launch {
            toggleFavorite(pokemon)
        }
    }
}