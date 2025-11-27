package com.cavies.pokedex.presentation.ui.components.collection

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cavies.pokedex.domain.model.Pokemon


@Composable
fun PokemonGrid(
    pokemons: List<Pokemon>,
    gridState: LazyGridState,
    modifier: Modifier = Modifier,
    onFavoriteClick: (Pokemon) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(pokemons) {
        preloadPokemonImages(context, pokemons)
    }

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