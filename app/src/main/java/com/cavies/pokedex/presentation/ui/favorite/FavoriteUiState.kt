package com.cavies.pokedex.presentation.ui.favorite

import com.cavies.pokedex.domain.model.Pokemon


data class FavoriteUiState(
    val isLoading: Boolean = false,
    val favoritePokemons: List<Pokemon> = emptyList(),
    val error: String? = null
)