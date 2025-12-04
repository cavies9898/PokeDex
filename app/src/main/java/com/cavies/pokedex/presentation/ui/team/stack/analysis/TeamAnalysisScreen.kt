package com.cavies.pokedex.presentation.ui.team.stack.analysis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// --- Datos de ejemplo (simulando los valores de la imagen) ---
data class TeamAnalysisData(
    val physicalAttack: Float = 0.2f, // 1F / 5E -> ~16.7% Físico
    val speedFast: Float = 0.33f, // 2 de 6
    val speedMedium: Float = 0.50f, // 3 de 6
    val speedSlow: Float = 0.17f, // 1 de 6
    val uniqueTypes: Int = 7,
    val defensivePokemon: Int = 1
)

@Composable
fun TeamAnalysisScreen() {
    val data = TeamAnalysisData()
    // Colores base para simular la app
    val primaryColor = Color(0xFF673AB7) // Un morado
    val successColor = Color(0xFF4CAF50) // Verde
    val warningColor = Color(0xFFFF9800) // Naranja
    val backgroundColor = Color(0xFFF5F5F5) // Fondo claro

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 1. Análisis del Equipo Header
        TeamAnalysisHeader(primaryColor)
        Spacer(modifier = Modifier.height(16.dp))

        // 2. Fortalezas
        StrengthCard(successColor)
        Spacer(modifier = Modifier.height(16.dp))

        // 3. Debilidades
        WeaknessCard(warningColor)
        Spacer(modifier = Modifier.height(24.dp))

        // 4. Composición del Equipo
        TeamCompositionCard(data, primaryColor, successColor, warningColor)
    }
}

// Bloque 1: Análisis del Equipo Header
@Composable
fun TeamAnalysisHeader(primaryColor: Color) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = primaryColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Análisis del Equipo",
                style = MaterialTheme.typography.headlineSmall.copy(color = Color.White)
            )
            Text(
                text = "Evaluación automática basada en 6 Pokémon",
                style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f))
            )
        }
    }
}

// Bloque 2: Fortalezas Card
@Composable
fun StrengthCard(successColor: Color) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Fortalezas",
                    tint = successColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Fortalezas",
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Checkmark",
                    tint = successColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Balance entre atacantes físicos y especiales",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// Bloque 3: Debilidades Card
@Composable
fun WeaknessCard(warningColor: Color) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Debilidades",
                    tint = Color.Red, // Usar un rojo para el icono de advertencia
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Debilidades",
                    style = MaterialTheme.typography.titleMedium.copy(color = Color.Red)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Exclamación",
                    tint = warningColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Bajo poder ofensivo general",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

// Bloque 4: Composición del Equipo Card
@Composable
fun TeamCompositionCard(
    data: TeamAnalysisData,
    primaryColor: Color,
    successColor: Color,
    warningColor: Color
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Composición del Equipo",
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Tipo de Ataque (Barra dividida)
            AttackTypeSection(data, primaryColor)
            Spacer(modifier = Modifier.height(16.dp))

            // Niveles de Velocidad (Barra tricolor)
            SpeedLevelSection(data, successColor, warningColor)
            Spacer(modifier = Modifier.height(16.dp))

            // Tipos únicos
            CompositionDetailRow("Tipos únicos", data.uniqueTypes.toString())
            Spacer(modifier = Modifier.height(8.dp))

            // Pokémon defensivos
            CompositionDetailRow("Pokémon defensivos", data.defensivePokemon.toString())
        }
    }
}

// Componente para la barra de Tipo de Ataque
@Composable
fun AttackTypeSection(data: TeamAnalysisData, primaryColor: Color) {
    Column {
        Text(
            text = "Tipo de Ataque",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Indicador de texto (1F / 5E)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "1F / 5E",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        // Barra de progreso Físico/Especial
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(12.dp)) {
            // Físico (Naranja/Rojo)
            Box(
                modifier = Modifier
                    .weight(data.physicalAttack)
                    .fillMaxHeight()
                    .background(Color(0xFFE57373)) // Color Físico (Rojo claro)
            )
            // Especial (Azul/Morado)
            Box(
                modifier = Modifier
                    .weight(1f - data.physicalAttack)
                    .fillMaxHeight()
                    .background(primaryColor) // Color Especial (Morado)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Físico", style = MaterialTheme.typography.bodySmall)
            Text(text = "Especial", style = MaterialTheme.typography.bodySmall)
        }
    }
}

// Componente para la barra de Niveles de Velocidad
@Composable
fun SpeedLevelSection(data: TeamAnalysisData, successColor: Color, warningColor: Color) {
    val fastColor = successColor
    val mediumColor = warningColor
    val slowColor = Color.Red

    Column {
        Text(
            text = "Niveles de Velocidad",
            style = MaterialTheme.typography.bodyMedium.copy(color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(4.dp))

        // Indicador de texto (1R / 3M / 2L) - Ajustado a 2R/3M/1L para simular los pesos de la data
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "2R / 3M / 1L",
                style = MaterialTheme.typography.labelSmall.copy(color = Color.Gray)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        // Barra de Velocidad tricolor
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp)) // Borde redondeado para la barra
        ) {
            Box(
                modifier = Modifier
                    .weight(data.speedFast)
                    .fillMaxHeight()
                    .background(fastColor)
            )
            Box(
                modifier = Modifier
                    .weight(data.speedMedium)
                    .fillMaxHeight()
                    .background(mediumColor)
            )
            Box(
                modifier = Modifier
                    .weight(data.speedSlow)
                    .fillMaxHeight()
                    .background(slowColor)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))

        // Leyendas de velocidad y rangos
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            SpeedLabel("Rápido (100+)", fastColor)
            SpeedLabel("Medio (60-99)", mediumColor)
            SpeedLabel("Lento (<60)", slowColor)
        }
    }
}

@Composable
fun SpeedLabel(text: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Un punto de color para la leyenda
        Canvas(modifier = Modifier.size(6.dp), onDraw = {
            drawCircle(
                color = color,
                center = Offset(size.width / 2, size.height / 2),
                radius = size.minDimension / 2
            )
        })
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

// Componente para filas de detalle simple
@Composable
fun CompositionDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
        )
    }
    Divider()
}

@Preview(showBackground = true)
@Composable
fun TeamAnalysisPreview() {
    MaterialTheme {
        TeamAnalysisScreen()
    }
}