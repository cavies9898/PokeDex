package com.cavies.pokedex.presentation.ui.favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.cavies.pokedex.presentation.ui.components.collection.PokemonGrid
import com.cavies.pokedex.presentation.ui.favorite.common.FavoriteHeader

@Composable
fun FavoritesScreen() {
    val viewModel: FavoriteViewModel = hiltViewModel()
    val state = viewModel.uiState

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FavoriteHeader(
            pokemons = state.favoritePokemons.count()
        )

        PokemonGrid(
            pokemons = state.favoritePokemons,
            gridState = rememberLazyGridState(),
            onFavoriteClick = viewModel::onFavoriteClick
        )
    }
}
