package com.cavies.pokedex.presentation.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.presentation.ui.home.components.FilterBottomSheet
import com.cavies.pokedex.presentation.ui.home.components.FilterDialog
import com.cavies.pokedex.presentation.ui.home.components.HomeHeader
import com.cavies.pokedex.presentation.ui.home.components.PokemonCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val state = viewModel.uiState

    var showCategoryDialog by remember { mutableStateOf(false) }
    var showOptionsSheet by remember { mutableStateOf(false) }

    val gridState = rememberLazyGridState()

    LaunchedEffect(state.filteredPokemons) {
        gridState.scrollToItem(0)
    }

    if (showOptionsSheet) {
        ModalBottomSheet(
            onDismissRequest = { showOptionsSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            FilterBottomSheet(
                optionsList = state.categorySelected,
                selectedTypes = emptySet(),
                onApply = { selectedList ->
                    viewModel.filterByCategory(selectedList)
                    showOptionsSheet = false
                }
            )
        }
    }

    if (showCategoryDialog) {
        FilterDialog(
            onDismiss = { showCategoryDialog = false },
            onFilterSelected = { item ->
                showCategoryDialog = false
                viewModel.onFilterItemSelected(item)
                showOptionsSheet = item.hasOptions
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        HomeHeader(
            searchText = state.searchQuery,
            onSearchTextChange = viewModel::onSearchQueryChanged,
            onFilterClick = { showCategoryDialog = true },
            onSortAsc = { viewModel.sortAscending() },
            onSortDesc = { viewModel.sortDescending() }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading -> LoadingScreen()
                state.error != null -> Snackbar { Text(state.error) }
                else -> PokemonList(
                    pokemons = state.filteredPokemons,
                    gridState = gridState,
                    onFavoriteClick = viewModel::onFavoriteClick
                )
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator()
            Text("Descargando datos, por favor espera...")
        }
    }
}

@Composable
fun PokemonList(
    pokemons: List<Pokemon>,
    gridState: LazyGridState,
    modifier: Modifier = Modifier,
    onFavoriteClick: (Pokemon) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(pokemons, key = { it.id }) { pokemon ->
            PokemonCard(
                pokemon = pokemon,
                onClickItem = { Log.d("DEV_DEBUG", "${pokemon.name} Clicked") },
                onClickFavorite = { onFavoriteClick(pokemon) }
            )
        }
    }
}