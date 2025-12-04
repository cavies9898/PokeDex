package com.cavies.pokedex.presentation.ui.team.stack.coverage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


// Datos para las secciones "Fuerte Contra" y "Débil Contra"
data class ResistanceData(
    val type: String = "Type",
    val resistanceRatio: Float = 1f,
    val weaknessRatio: Float = 1f,
    val summary: String = "summary"        // e.g., "4R / 2D"
)

// Datos para la "Tabla Completa de Tipos"
data class TypeEffectiveness(
    val type: String,
    val greenCount: Int,  // Resistente
    val redCount: Int,    // Débil
    val greyCount: Int,   // Normal/Neutro
    val finalScore: Int   // Puntuación final (+2, -6, etc.)
)

val GreenResist = Color(0xFF66BB6A)   // Verde claro de las barras
val RedWeak = Color(0xFFE57373)       // Rojo claro de las barras
val GreyNeutral = Color(0xFFEEEEEE)
val DarkHeader = Color(0xFF43A047)
val RedHeader = Color(0xFFE53935)
val White = Color.White
val DarkGreyText = Color(0xFF424242)


@Composable
fun DualProgressBar(
    data: ResistanceData
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(GreyNeutral)
    ) {
        // Resistencia
        if (data.resistanceRatio > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(data.resistanceRatio)
                    .background(GreenResist)
            )
        }

        // Debilidad
        if (data.weaknessRatio > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(data.weaknessRatio)
                    .background(RedWeak)
            )
        }

        // Resto gris
        val remaining = 1f - (data.resistanceRatio + data.weaknessRatio)
        if (remaining > 0f) {
            Spacer(modifier = Modifier.weight(remaining))
        }
    }
}


@Composable
fun FullTableItem(data: TypeEffectiveness) {
    val total = (data.greenCount + data.redCount + data.greyCount).toFloat()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tipo de Pokémon (Texto)
        Text(
            data.type,
            modifier = Modifier
                .width(80.dp)
                .padding(vertical = 6.dp),
            style = MaterialTheme.typography.bodySmall
        )

        // Barra de Progreso 3 Colores
        Row(
            modifier = Modifier
                .weight(1f)
                .height(10.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            // Verde
            if (data.greenCount > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(data.greenCount / total)
                        .background(GreenResist)
                )
            }
            // Rojo
            if (data.redCount > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(data.redCount / total)
                        .background(RedWeak)
                )
            }
            // Gris
            if (data.greyCount > 0) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(data.greyCount / total)
                        .background(GreyNeutral)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Puntuación Detallada (4/0/2)
        Text(
            "${data.greenCount}/${data.redCount}/${data.greyCount}",
            modifier = Modifier.width(50.dp),
            style = MaterialTheme.typography.bodySmall
        )

        // Puntuación Final (+2)
        val scoreColor = when {
            data.finalScore > 0 -> GreenResist
            data.finalScore < 0 -> RedWeak
            else -> DarkGreyText
        }
        Text(
            if (data.finalScore > 0) "+${data.finalScore}" else data.finalScore.toString(),
            modifier = Modifier.width(30.dp),
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Bold,
            color = scoreColor
        )
    }
}


@Preview
@Composable
fun TeamCoverageScreen() {
    val strongAgainstData = listOf(
        ResistanceData("Grass", 4f / 6f, 2f / 6f, "4R / 2D"),
        ResistanceData("Fighting", 2f / 3f, 1f / 3f, "2R / 0D"),
        ResistanceData("Bug", 3f / 4f, 1f / 4f, "3R / 1D"),
        ResistanceData("Ground", 2f / 3f, 1f / 3f, "2R / 1D"),
        ResistanceData("Dark", 1f / 2f, 0f / 2f, "1R / 0D")
    )

    val weakAgainstData = listOf(
        ResistanceData("Electric", 0f, 5f / 5f, "0R / 5D"),
        ResistanceData("Ice", 0f, 3f / 3f, "0R / 3D"),
        ResistanceData("Flying", 0f, 2f / 2f, "0R / 2D"),
        ResistanceData("Poison", 0f, 1f / 1f, "0R / 1D")
    )

    val fullTableData = listOf(
        TypeEffectiveness("Grass", 4, 0, 2, 2),
        TypeEffectiveness("Fighting", 2, 4, 0, 2),
        TypeEffectiveness("Bug", 3, 2, 1, 1),
        TypeEffectiveness("Ground", 1, 5, 0, 1),
        TypeEffectiveness("Dark", 1, 5, 0, 1),
        TypeEffectiveness("Steel", 1, 5, 0, 1),
        TypeEffectiveness("Fairy", 1, 5, 0, 1),
        TypeEffectiveness("Normal", 0, 6, 0, 0),
        TypeEffectiveness("Fire", 2, 2, 2, 0),
        TypeEffectiveness("Water", 1, 4, 1, 0),
        TypeEffectiveness("Psychic", 1, 4, 1, 0),
        TypeEffectiveness("Rock", 3, 0, 3, 0),
        TypeEffectiveness("Ghost", 0, 6, 0, 0),
        TypeEffectiveness("Dragon", 0, 6, 0, 0),
        TypeEffectiveness("Poison", 0, 5, 1, -1),
        TypeEffectiveness("Flying", 0, 4, 2, -2),
        TypeEffectiveness("Ice", 0, 3, 3, -3),
        TypeEffectiveness("Electric", 0, 1, 5, -5)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // --- 1. CABECERAS DE RESUMEN ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Tipos Resistidos (5)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = DarkHeader)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Tipos Resistidos",
                        color = White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("5", color = White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                }
            }
            // Debilidades (4)
            Card(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                colors = CardDefaults.cardColors(containerColor = RedHeader)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "Debilidades",
                        color = White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("4", color = White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                }
            }
        }


        // --- 2. FUERTE CONTRA ---
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    "✔️ Fuerte Contra",
                    fontWeight = FontWeight.Bold,
                    color = GreenResist
                )

                Spacer(modifier = Modifier.height(12.dp))

                strongAgainstData.forEach { data ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),   // separa filas
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Columna 1: Tipo (fijo)
                        Text(
                            data.type,
                            modifier = Modifier.width(80.dp),
                            style = MaterialTheme.typography.bodySmall
                        )

                        // Columna 2: Barra (ocupa resto del espacio)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        ) {
                            DualProgressBar(data)
                        }

                        // Columna 3: Resumen
                        Text(
                            data.summary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }


        // --- 3. DÉBIL CONTRA ---

        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("⚠️ Débil Contra", fontWeight = FontWeight.Bold, color = RedWeak)
                Spacer(modifier = Modifier.height(8.dp))

                strongAgainstData.forEach { data ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),   // separa filas
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Columna 1: Tipo (fijo)
                        Text(
                            data.type,
                            modifier = Modifier.width(80.dp),
                            style = MaterialTheme.typography.bodySmall
                        )

                        // Columna 2: Barra (ocupa resto del espacio)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                        ) {
                            DualProgressBar(data)
                        }

                        // Columna 3: Resumen
                        Text(
                            data.summary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // --- 4. TABLA COMPLETA DE TIPOS ---
        Card(
            modifier = Modifier
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Tabla Completa de Tipos", fontWeight = FontWeight.Bold, color = DarkGreyText)
                Spacer(modifier = Modifier.height(8.dp))

                fullTableData.forEach { data ->
                    FullTableItem(data = data)
                }

                // Leyenda
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(GreenResist)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "Resistente",
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(RedWeak)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Débil")

                    Text("R/N/D")
                }
            }
        }
    }
}