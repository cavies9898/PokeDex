package com.cavies.pokedex.presentation.ui.components.collection

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cavies.pokedex.domain.model.Pokemon


@Composable
fun PokemonGrid(
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
                onClickItem = { },
                onClickFavorite = { onFavoriteClick(pokemon) },
                modifier = Modifier
            )
        }
    }
}