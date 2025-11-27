package com.cavies.pokedex.presentation.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cavies.pokedex.domain.model.Categories
import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.domain.usecase.GetCategoriesUseCase
import com.cavies.pokedex.domain.usecase.GetPokemonsUseCase
import com.cavies.pokedex.domain.usecase.ToggleFavoriteUseCase
import com.cavies.pokedex.presentation.ui.home.common.filter.FilterItem
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPokemons: GetPokemonsUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    private val getCategories: GetCategoriesUseCase
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    private var cachedCategories: Categories? = null

    init {
        loadPokemons()
    }

    private fun loadPokemons() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)
            try {
                val pokemons = getPokemons()
                uiState = uiState.copy(
                    pokemons = pokemons,
                    filteredPokemons = pokemons,
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

    private suspend fun getCategoriesCached(): Categories {
        return cachedCategories ?: getCategories().also { cachedCategories = it }
    }

    fun onFavoriteClick(pokemon: Pokemon) {
        viewModelScope.launch {
            toggleFavorite(pokemon)
            val toggle: (Pokemon) -> Pokemon = {
                if (it.id == pokemon.id) it.copy(isFavorite = !it.isFavorite) else it
            }
            uiState = uiState.copy(
                pokemons = uiState.pokemons.map(toggle),
                filteredPokemons = uiState.filteredPokemons.map(toggle)
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        uiState = uiState.copy(searchQuery = query)
        applyFilters()
    }

    fun onFilterItemSelected(item: FilterItem) {
        viewModelScope.launch {
            item.directFilter?.let { direct ->
                val result = direct(uiState.pokemons)
                uiState = uiState.copy(
                    filteredPokemons = result,
                    currentFilterItem = item,
                    categorySelected = emptyList()
                )
                return@launch
            }

            if (item == FilterItem.All) {
                uiState = uiState.copy(
                    filteredPokemons = uiState.pokemons,
                    currentFilterItem = item,
                    categorySelected = emptyList()
                )
                return@launch
            }

            val categories = getCategoriesCached()
            val values = when (item) {
                FilterItem.Type -> categories.type
                FilterItem.Generation -> categories.generation
                FilterItem.Region -> categories.region
                FilterItem.Habitat -> categories.habitat
                FilterItem.Color -> categories.color
                FilterItem.Shape -> categories.shape
                else -> emptyList()
            }

            uiState = uiState.copy(
                currentFilterItem = item,
                categorySelected = values
            )
        }
    }

    fun filterByCategory(selectedOptions: List<String>) {
        uiState = uiState.copy(categorySelected = selectedOptions)
        applyFilters()
    }

    fun sortAscending() = applySorting(asc = true)
    fun sortDescending() = applySorting(asc = false)

    private fun applySorting(asc: Boolean) {
        val current = uiState.currentFilterItem
        val sorted = uiState.filteredPokemons.sortedWith(
            compareBy<Pokemon> {
                when (current) {
                    FilterItem.HP -> it.stats.hp
                    FilterItem.Attack -> it.stats.attack
                    FilterItem.Defense -> it.stats.defense
                    FilterItem.SpecialAttack -> it.stats.specialAttack
                    FilterItem.SpecialDefense -> it.stats.specialDefense
                    FilterItem.Speed -> it.stats.speed
                    FilterItem.Id -> it.id
                    FilterItem.Alphabet -> it.name
                    FilterItem.Total -> it.stats.total
                    else -> it.id
                }
            }.let { cmp -> if (asc) cmp else cmp.reversed() }
        )
        uiState = uiState.copy(filteredPokemons = sorted)
    }

    private fun applyFilters() {
        val filter = uiState.currentFilterItem
        val selectedOptions = uiState.categorySelected.toSet()
        val query = uiState.searchQuery.trim()

        val filtered = uiState.pokemons.filter { p ->
            val matchesFilter = when {
                filter == null || filter == FilterItem.All -> true
                filter.selector != null -> {
                    val values = filter.selector(p)
                    values.any(selectedOptions::contains)
                }

                else -> true
            }

            val matchesSearch =
                if (query.isBlank()) true else p.name.contains(query, ignoreCase = true)

            matchesFilter && matchesSearch
        }

        uiState = uiState.copy(filteredPokemons = filtered)
    }
}