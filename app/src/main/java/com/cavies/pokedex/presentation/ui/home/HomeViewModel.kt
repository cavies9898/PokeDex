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
    private val getCategories: GetCategoriesUseCase,
) : ViewModel() {

    var uiState by mutableStateOf(HomeUiState())
        private set

    private var cachedCategories: Categories? = null

    init {
        observePokemons()
    }

    private fun observePokemons() {
        viewModelScope.launch {
            getPokemons().collect { list ->
                uiState = uiState.copy(
                    pokemons = list,
                    filteredPokemons = list,
                    isLoading = false,
                    error = null
                )
            }
        }
    }

    private suspend fun getCategoriesCached() =
        cachedCategories ?: getCategories().also { cachedCategories = it }

    fun onFavoriteClick(pokemon: Pokemon) {
        viewModelScope.launch {
            toggleFavorite(pokemon)
        }
    }

    fun onSearchQueryChanged(query: String) {
        uiState = uiState.copy(searchQuery = query)
        applyFiltersAndSorting()
    }

    fun onFilterItemSelected(item: FilterItem) {
        viewModelScope.launch {
            item.directFilter?.let { direct ->
                uiState = uiState.copy(
                    currentFilterItem = item,
                    categorySelected = emptyList(),
                    filteredPokemons = direct(uiState.pokemons)
                )
                return@launch
            }

            if (!item.hasOptions || item == FilterItem.All) {
                uiState = uiState.copy(
                    currentFilterItem = item,
                    categorySelected = emptyList(),
                    filteredPokemons = uiState.pokemons
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

    fun filterByCategory(selected: List<String>) {
        uiState = uiState.copy(categorySelected = selected)
        applyFiltersAndSorting()
    }


    fun sortAscending() = applySorting(true)
    fun sortDescending() = applySorting(false)

    private fun applySorting(asc: Boolean) {
        val filter = uiState.currentFilterItem ?: FilterItem.Id

        val comparator = when (filter) {
            FilterItem.HP -> compareBy<Pokemon> { it.stats.hp }
            FilterItem.Attack -> compareBy { it.stats.attack }
            FilterItem.Defense -> compareBy { it.stats.defense }
            FilterItem.SpecialAttack -> compareBy { it.stats.specialAttack }
            FilterItem.SpecialDefense -> compareBy { it.stats.specialDefense }
            FilterItem.Speed -> compareBy { it.stats.speed }
            FilterItem.Alphabet -> compareBy { it.name }
            FilterItem.Total -> compareBy { it.stats.total }
            FilterItem.Id, FilterItem.All -> compareBy { it.id }
            else -> return
        }

        val sorted = if (asc) {
            uiState.filteredPokemons.sortedWith(comparator)
        } else {
            uiState.filteredPokemons.sortedWith(comparator.reversed())
        }

        uiState = uiState.copy(filteredPokemons = sorted)
    }


    private fun applyFiltersAndSorting() {
        val filter = uiState.currentFilterItem
        val query = uiState.searchQuery.trim().lowercase()
        val selected = uiState.categorySelected.toSet()

        val filtered = uiState.pokemons.filter { p ->
            val matchesFilter = when {
                filter?.selector == null -> true
                selected.isEmpty() -> true
                else -> filter.selector(p).any(selected::contains)
            }
            val matchesSearch = p.name.contains(query, ignoreCase = true)

            matchesFilter && matchesSearch
        }

        uiState = uiState.copy(filteredPokemons = filtered)
    }
}