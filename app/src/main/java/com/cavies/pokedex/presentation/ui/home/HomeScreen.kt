package com.cavies.pokedex.presentation.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.presentation.ui.components.collection.PokemonGrid
import com.cavies.pokedex.presentation.ui.components.overlay.Loading
import com.cavies.pokedex.presentation.ui.home.common.HomeHeader
import com.cavies.pokedex.presentation.ui.home.common.filter.FilterBottomSheet
import com.cavies.pokedex.presentation.ui.home.common.filter.FilterDialog
import com.cavies.pokedex.presentation.ui.home.common.filter.FilterItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state = viewModel.uiState
    val gridState = viewModel.gridState

    var categoryDialogVisible by remember { mutableStateOf(false) }
    var optionsSheetVisible by remember { mutableStateOf(false) }

    // Scroll reset
    HandleScrollReset(state.shouldResetScroll, gridState) { viewModel.consumeScrollResetFlag() }

    // Dialogs
    CategoryFilterDialog(
        visible = categoryDialogVisible,
        onDismiss = { categoryDialogVisible = false },
        onFilterSelected = { item ->
            categoryDialogVisible = false
            viewModel.onFilterItemSelected(item)
            optionsSheetVisible = item.hasOptions
        }
    )

    OptionsBottomSheet(
        visible = optionsSheetVisible,
        optionsList = state.categorySelected,
        onApply = { selectedList ->
            viewModel.filterByCategory(selectedList)
            optionsSheetVisible = false
        }
    )

    // UI
    Column(modifier = Modifier.fillMaxSize()) {
        HomeHeader(
            searchText = state.searchQuery,
            onSearchTextChange = viewModel::onSearchQueryChanged,
            onFilterClick = { categoryDialogVisible = true },
            onSortAsc = viewModel::sortAscending,
            onSortDesc = viewModel::sortDescending
        )

        PokemonGridWithFab(
            state = state,
            gridState = gridState,
            onFavoriteClick = viewModel::onFavoriteClick
        )
    }
}

@Composable
private fun HandleScrollReset(
    shouldReset: Boolean,
    gridState: LazyGridState,
    onConsumed: () -> Unit
) {
    LaunchedEffect(shouldReset) {
        if (shouldReset) {
            gridState.scrollToItem(0)
            onConsumed()
        }
    }
}

@Composable
private fun CategoryFilterDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onFilterSelected: (FilterItem) -> Unit
) {
    if (!visible) return
    FilterDialog(onDismiss = onDismiss, onFilterSelected = onFilterSelected)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OptionsBottomSheet(
    visible: Boolean,
    optionsList: List<String>,
    onApply: (List<String>) -> Unit
) {
    if (!visible) return
    ModalBottomSheet(
        onDismissRequest = { /* handled outside */ },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        FilterBottomSheet(
            optionsList = optionsList,
            selectedTypes = emptySet(),
            onApply = onApply
        )
    }
}

@Composable
private fun PokemonGridWithFab(
    state: HomeUiState,
    gridState: LazyGridState,
    onFavoriteClick: (Pokemon) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> Loading()
            state.error != null -> Snackbar { Text(state.error) }
            else -> PokemonGrid(
                pokemons = state.filteredPokemons,
                gridState = gridState,
                onFavoriteClick = onFavoriteClick
            )
        }

        val showFab by remember { derivedStateOf { gridState.firstVisibleItemIndex > 3 } }
        val scope = rememberCoroutineScope()

        AnimatedVisibility(
            visible = showFab,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            enter = slideInVertically { it } + fadeIn(),
            exit = slideOutVertically { it } + fadeOut()
        ) {
            FloatingActionButton(
                onClick = { scope.launch { gridState.animateScrollToItem(0) } },
                containerColor = Color(0xFFE53935),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = "Scroll to top",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
