package com.cavies.pokedex.presentation.ui.team

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.cavies.pokedex.presentation.ui.components.animations.shrinkClick

@Composable
fun TeamScreen(
    viewModel: TeamViewModel = hiltViewModel(),
    onTeamBuilderClick: () -> Unit,
) {
    val charizardImageId =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/6.png"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TeamHeader()

        CreateNewTeamButton(
            onClick = { onTeamBuilderClick() },
            modifier = Modifier.padding(20.dp)
        )

        TeamCard(
            teamName = "Equipo Dinamita",
            currentPokemonCount = 1,
            maxPokemonCount = 6,
            charizardImageId = charizardImageId,
            onEditClick = {},
            onDeleteClick = {},
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

/**
 * Botón grande para crear un nuevo equipo.
 */
@Composable
fun CreateNewTeamButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { },
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        contentPadding = PaddingValues(vertical = 20.dp),
        modifier = modifier
            .fillMaxWidth()
            .shrinkClick { onClick() }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Crear Nuevo Equipo",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Crear Nuevo Equipo",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}


/**
 * Tarjeta que representa un equipo individual.
 */
@Composable
fun TeamCard(
    teamName: String,
    currentPokemonCount: Int,
    maxPokemonCount: Int,
    charizardImageId: String,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = teamName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$currentPokemonCount/$maxPokemonCount Pokémon",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar Equipo",
                    tint = Color.Blue,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onEditClick)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar Equipo",
                    tint = Color.Red,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onDeleteClick)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            PokemonSlotRow(
                charizardImageId = charizardImageId,
                currentCount = currentPokemonCount,
                maxCount = maxPokemonCount
            )
        }
    }
}

/**
 * Fila que muestra los slots de los 6 Pokémon y la barra de progreso.
 */
@Composable
fun PokemonSlotRow(charizardImageId: String, currentCount: Int, maxCount: Int) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Genera los 6 slots
            repeat(maxCount) { index ->
                if (index < currentCount) {
                    // Slot Ocupado (Charizard) o pokemon
                    Image(
                        painter = rememberAsyncImagePainter(charizardImageId),
                        contentDescription = "Pokémon slot ocupado",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(2.dp, Color.Blue, RoundedCornerShape(8.dp)) // Borde de énfasis
                    )
                } else {
                    // Slot Vacío
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF0F0F8))
                            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        val progress = currentCount.toFloat() / maxCount.toFloat()
        LinearProgressIndicator(
            progress = { progress },
            color = Color(0xFF2563EB),
            trackColor = Color(0xFFF0F0F8),
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}