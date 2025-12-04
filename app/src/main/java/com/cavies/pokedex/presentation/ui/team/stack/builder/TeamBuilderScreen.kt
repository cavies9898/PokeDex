package com.cavies.pokedex.presentation.ui.team.stack.builder

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cavies.pokedex.presentation.navigation.TeamScreens
import com.cavies.pokedex.presentation.ui.components.animations.shrinkClick
import com.cavies.pokedex.presentation.ui.components.shapes.RoundedShape
import com.cavies.pokedex.presentation.ui.team.stack.TeamStackViewModel

@Composable
fun TeamBuilderScreen(
    viewModel: TeamStackViewModel = hiltViewModel(),
) {
    Column(modifier = Modifier.fillMaxSize()) {
        PokemonListView(
            pokemons = pokemonList,
            onAddPokemon = {},
            onEditPokemon = {},
            onDeletePokemon = {}
        )
    }
}

@Composable
fun TeamBuilderHeader(
    onBackClick: () -> Unit,
    onScreenChanged: (screen: String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedShape(bottomRadius = 60f))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color(0xFF38BDF8),
                        Color(0xFF2563EB)
                    )
                )
            )
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(top = 12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .size(32.dp)
                        .shrinkClick { onBackClick() },
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "back",
                        tint = Color.White,
                    )
                }
                Text(
                    text = "Equipo 3",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                Text(
                    text = "0/6",
                    color = Color.White,
                    fontSize = 12.sp,
                )
            }
            LazyRow(
                modifier = Modifier.padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val listButtons = listOf(
                    TeamScreens.TEAM,
                    TeamScreens.STATS,
                    TeamScreens.COVERAGE,
                    TeamScreens.ANALYSIS
                )
                items(listButtons) { item ->
                    Button(
                        onClick = { onScreenChanged(item) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.25f)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = item,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 14.dp)
                        )
                    }
                }
            }
        }
    }
}

// Definición de datos para un Pokémon
data class Pokemon(
    val name: String,
    val type1: String,
    val type2: String? = null,
    val urlImage: String
)

// Datos de ejemplo
val pokemonList = listOf(
    Pokemon(
        "Bulbasaur",
        "Grass",
        "Poison",
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"
    ),
    Pokemon(
        "Charmander",
        "Fire",
        null,
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png"
    )
)

@Composable
fun PokemonCard(
    pokemon: Pokemon,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(pokemon.urlImage),
                    contentDescription = "${pokemon.name} image",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 12.dp)
                )

                Column {
                    Text(
                        text = pokemon.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TypeChip(type = pokemon.type1)
                        pokemon.type2?.let { TypeChip(type = it) }
                    }
                }
            }

            // 2. Iconos de Editar y Eliminar
//            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
//                // Icono de Editar
//                ActionIcon(icon = Icons.Default.Create, color = Color(0xFF6A6A6A), onClick = onEdit)
//                // Icono de Eliminar
//                ActionIcon(
//                    icon = Icons.Default.Delete,
//                    color = Color(0xFFD62A2A),
//                    onClick = onDelete
//                )
//            }
        }
    }
}

@Composable
fun TypeChip(type: String) {
    val (color, textColor) = when (type) {
        "Grass" -> Pair(Color(0xFF6AC24A), Color.White)
        "Poison" -> Pair(Color(0xFF9E4A98), Color.White)
        "Fire" -> Pair(Color(0xFFD6732B), Color.White)
        else -> Pair(Color.Gray, Color.White)
    }

    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(10.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = type,
            color = textColor,
            fontSize = 12.sp,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun ActionIcon(icon: ImageVector, color: Color, onClick: () -> Unit) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        tint = color,
        modifier = Modifier
            .size(24.dp)
            .clickable(onClick = onClick)
    )
}

@Composable
fun AddPokemonButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .padding(top = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.Transparent),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = SolidColor(Color(0xFF4285F4)),
            width = 2.dp
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Agregar Pokémon",
                tint = Color(0xFF4285F4)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Agregar Pokémon",
                color = Color(0xFF4285F4),
                fontSize = 16.sp
            )
        }
    }
}

// --- Vista Completa ---
@Composable
fun PokemonListView(
    pokemons: List<Pokemon>,
    onAddPokemon: () -> Unit,
    onEditPokemon: (Pokemon) -> Unit,
    onDeletePokemon: (Pokemon) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F8))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(pokemons) { pokemon ->
            PokemonCard(
                pokemon = pokemon,
                onEdit = { onEditPokemon(pokemon) },
                onDelete = { onDeletePokemon(pokemon) }
            )
        }

        item {
            AddPokemonButton(onClick = onAddPokemon)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPokemonListView() {
    // IMPORTANTE: Este preview fallará si no has añadido las imágenes a la carpeta 'res/drawable'
    // con los nombres usados en la lista de ejemplo (por ejemplo, 'bulbasaur_image.png').
    // Si no tienes las imágenes, la aplicación fallará al intentar cargar el recurso.
    // Para fines de PREVIEW temporal, podrías necesitar una imagen de placeholder si no quieres fallar.

    // Asumiendo que el recurso existe:
    // PokemonListView(
    //     pokemons = pokemonList,
    //     onAddPokemon = { /* */ },
    //     onEditPokemon = { /* */ },
    //     onDeletePokemon = { /* */ }
    // )

    // Si estás probando y no tienes las imágenes, puedes usar iconos de Material para simular:
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            "Vista simulada sin imágenes de recursos para el Preview",
            Modifier.padding(bottom = 8.dp)
        )
        PokemonCard(
            pokemon = pokemonList[0].copy(urlImage = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"),
            onEdit = {},
            onDelete = {}
        )
        PokemonCard(
            pokemon = pokemonList[1].copy(urlImage = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png"),
            onEdit = {},
            onDelete = {}
        )
        AddPokemonButton(onClick = {})
    }
}