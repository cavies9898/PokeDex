package com.cavies.pokedex.presentation.ui.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cavies.pokedex.domain.model.Pokemon
import com.cavies.pokedex.presentation.components.PokedexHeader

@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val state = viewModel.uiState

    Column(modifier = Modifier.fillMaxSize()) {

        PokedexHeader(
            searchText = state.searchQuery,
            onSearchTextChange = viewModel::onSearchQueryChanged
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.isLoading -> CircularProgressIndicator()
                state.error != null -> Text("Error: ${state.error}")
                else -> PokemonList(state.filteredPokemons)
            }
        }
    }
}

@Composable
fun PokemonList(pokemons: List<Pokemon>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(pokemons) { pokemon ->
            PokemonCard(pokemon)
            Log.d("Pokemon - ${pokemon.name}", "${pokemon.types.map { it }}")
        }
    }
}
@Composable
fun PokemonCard(pokemon: Pokemon) {
    val pokemonTypeColors = mapOf(
        "normal" to Color(0xFFA8A77A),
        "fire" to Color(0xFFEE8130),
        "water" to Color(0xFF6390F0),
        "electric" to Color(0xFFF7D02C),
        "grass" to Color(0xFF7AC74C),
        "ice" to Color(0xFF96D9D6),
        "fighting" to Color(0xFFC22E28),
        "poison" to Color(0xFFA33EA1),
        "ground" to Color(0xFFE2BF65),
        "flying" to Color(0xFFA98FF3),
        "psychic" to Color(0xFFF95587),
        "bug" to Color(0xFFA6B91A),
        "rock" to Color(0xFFB6A136),
        "ghost" to Color(0xFF735797),
        "dragon" to Color(0xFF6F35FC),
        "dark" to Color(0xFF705746),
        "steel" to Color(0xFFB7B7CE),
        "fairy" to Color(0xFFD685AD)
    )

    val backgroundColor = pokemonTypeColors[pokemon.types.firstOrNull()] ?: Color.Gray

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = backgroundColor
        )
    ) {
        // 2. Contenido interno de la tarjeta
        Box(modifier = Modifier.fillMaxSize()) {

            // Botón de Corazón (parte superior derecha
            IconButton(
                onClick = { /* Aquí puedes manejar el click para "favorito" */ },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Favorito",
                    tint = Color.Red
                )
            }

            // Contenido principal (Columna)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {

                // Número del Pokémon
                Text(
                    text = "#${pokemon.id}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Imagen del Pokémon (centrada)
                Image(
                    painter = rememberAsyncImagePainter(pokemon.imageUrl),
                    contentDescription = pokemon.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .height(150.dp)  // más alto
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .padding(bottom = 4.dp)
                )


                // Nombre del Pokémon
                Text(
                    text = pokemon.name,
                    color = Color.White,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Fichas de Tipo (Row horizontal)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    pokemon.types.forEach { type ->
                        val background = pokemonTypeColors[type] ?: Color.Gray // Gris por defecto si no existe
                        TypeChip(typeName = type, backgroundColor = background)
                    }
                }
            }
        }
    }
}

@Composable
fun TypeChip(typeName: String, backgroundColor: Color) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor.copy(alpha = 0.8f))
    ) {
        Text(
            text = typeName,
            color = Color.White,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}