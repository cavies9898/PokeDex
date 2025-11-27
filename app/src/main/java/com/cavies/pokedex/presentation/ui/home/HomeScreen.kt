package com.cavies.pokedex.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cavies.pokedex.presentation.ui.components.overlay.Loading
import com.cavies.pokedex.presentation.ui.components.collection.PokemonGrid
import com.cavies.pokedex.presentation.ui.home.common.filter.FilterBottomSheet
import com.cavies.pokedex.presentation.ui.home.common.filter.FilterDialog
import com.cavies.pokedex.presentation.ui.home.common.HomeHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val state = viewModel.uiState

    var categoryDialogVisible by remember { mutableStateOf(false) }
    var optionsSheetVisible by remember { mutableStateOf(false) }

    val gridState = rememberLazyGridState()

    LaunchedEffect(state.filteredPokemons) {
        gridState.scrollToItem(0)
    }

    if (categoryDialogVisible) {
        FilterDialog(
            onDismiss = { categoryDialogVisible = false },
            onFilterSelected = { item ->
                categoryDialogVisible = false
                viewModel.onFilterItemSelected(item)

                optionsSheetVisible = item.hasOptions
            }
        )
    }

    if (optionsSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { optionsSheetVisible = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            FilterBottomSheet(
                optionsList = state.categorySelected,
                selectedTypes = emptySet(),
                onApply = { selectedList ->
                    viewModel.filterByCategory(selectedList)
                    optionsSheetVisible = false
                }
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        HomeHeader(
            searchText = state.searchQuery,
            onSearchTextChange = viewModel::onSearchQueryChanged,
            onFilterClick = { categoryDialogVisible = true },
            onSortAsc = viewModel::sortAscending,
            onSortDesc = viewModel::sortDescending
        )

        Box(modifier = Modifier.fillMaxSize()) {

            when {
                state.isLoading -> Loading()
                state.error != null -> Snackbar { Text(state.error) }
                else -> PokemonGrid(
                    pokemons = state.filteredPokemons,
                    gridState = gridState,
                    onFavoriteClick = viewModel::onFavoriteClick
                )
            }
        }
    }
}