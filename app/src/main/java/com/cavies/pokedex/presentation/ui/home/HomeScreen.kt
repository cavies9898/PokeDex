package com.cavies.pokedex.presentation.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.presentation.ui.home.components.HomeHeader
import com.cavies.pokedex.presentation.ui.home.components.PokemonCard

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val state = viewModel.uiState

    Column(modifier = Modifier.fillMaxSize()) {

        HomeHeader(
            searchText = state.searchQuery,
            onSearchTextChange = viewModel::onSearchQueryChanged
        )

        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            when {
                state.isLoading -> ProgressIndicatorDialog()
                state.error != null -> Snackbar {}
                else -> PokemonList(state.filteredPokemons) { pokemon ->
                    viewModel.onFavoriteClick(pokemon)
                }
            }
        }
    }
}

@Composable
fun ProgressIndicatorDialog() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Descargando Datos!") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Esto podría demorar unos minutos")
                    CircularProgressIndicator()
                }
            },
            confirmButton = {}
        )
    }
}

@Composable
fun PokemonList(
    pokemons: List<Pokemon>,
    modifier: Modifier = Modifier,
    onFavoriteClick: (pokemon: Pokemon) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        items(pokemons) { pokemon ->
            PokemonCard(
                pokemon = pokemon,
                onClickItem = {
                    Log.d("DEV_DEBUG", "${pokemon.name} Clicked")
                },
                onClickFavorite = {
                    onFavoriteClick(pokemon)
                }
            )
        }
    }
}