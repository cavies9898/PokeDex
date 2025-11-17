package com.cavies.pokedex.presentation.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.usecase.GetPokemonsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPokemonsUseCase: GetPokemonsUseCase
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    init {
        loadPokemons()
    }

    private fun loadPokemons() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val pokemons = getPokemonsUseCase()

                uiState = uiState.copy(
                    isLoading = false,
                    pokemons = pokemons,
                    filteredPokemons = pokemons
                )
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, error = e.message)
            }
        }
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
    val pokemons: List<Pokemon> = emptyList(),
    val filteredPokemons: List<Pokemon> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null
)

