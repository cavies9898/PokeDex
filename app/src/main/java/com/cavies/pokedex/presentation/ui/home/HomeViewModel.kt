package com.cavies.pokedex.presentation.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.usecase.FilterByTypeUseCase
import com.cavies.pokedex.domain.usecase.GetPokemonsUseCase
import com.cavies.pokedex.domain.usecase.GetTypesUseCase
import com.cavies.pokedex.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPokemons: GetPokemonsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val getTypes: GetTypesUseCase,
    private val filterByType: FilterByTypeUseCase
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadPokemons()
        loadTypes()
    }

    private fun loadPokemons() {
        viewModelScope.launch {
            val pokemons = getPokemons()
            uiState = uiState.copy(
                pokemons = pokemons,
                filteredPokemons = pokemons
            )
        }
    }

    private fun loadTypes() {
        viewModelScope.launch {
            uiState = uiState.copy(types = getTypes())
        }
    }

    fun onFavoriteClick(pokemon: Pokemon) {
        viewModelScope.launch {
            toggleFavorite(pokemon)
            loadPokemons()
        }
    }

    fun onTypeSelected(type: String) {
        uiState = uiState.copy(
            filteredPokemons = filterByType(uiState.pokemons, type)
        )
    }

    fun onSearchQueryChanged(query: String) {
        uiState = uiState.copy(searchQuery = query)

        val filtered = if (query.isBlank()) {
            uiState.pokemons
        } else {
            uiState.pokemons.filter {
                it.name.contains(query.trim(), ignoreCase = true)
            }
        }

        uiState = uiState.copy(filteredPokemons = filtered)
    }
}



data class HomeUiState(
    val isLoading: Boolean = false,
    val types: List<String> = emptyList(),
    val pokemons: List<Pokemon> = emptyList(),
    val filteredPokemons: List<Pokemon> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null
)

