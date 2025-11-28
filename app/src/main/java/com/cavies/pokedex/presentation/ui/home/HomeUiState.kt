package com.cavies.pokedex.presentation.ui.home

import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.presentation.ui.home.common.filter.FilterItem

data class HomeUiState(
    val isLoading: Boolean = false,
    val pokemons: List<Pokemon> = emptyList(),
    val filteredPokemons: List<Pokemon> = emptyList(),
    val categorySelected: List<String> = emptyList(),
    val currentFilterItem: FilterItem? = null,
    val searchQuery: String = "",
    val shouldResetScroll: Boolean = false,
    val error: String? = null
)