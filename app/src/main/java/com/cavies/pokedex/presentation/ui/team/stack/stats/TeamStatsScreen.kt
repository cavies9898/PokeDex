package com.cavies.pokedex.presentation.ui.team.stack.stats

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cavies.pokedex.domain.model.PokemonStats
import kotlin.math.cos
import kotlin.math.sin

/**
 * Encabezado Azul del Equipo
 */
@Composable
fun StatsHeader(totalScore: Int, pokemonCount: Int = 6) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF38BDF8),
                            Color(0xFF9933FF)
                        )
                    )
                )
                .clip(RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Promedio Total del Equipo",
                color = Color.White,
                fontSize = 14.sp
            )
            Text(
                text = totalScore.toString(),
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = "Basado en $pokemonCount Pokémon",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp
            )
        }
    }
}

/**
 * Placeholder para el Gráfico de Radar
 */
@Composable
fun RadarChartCard(stats: PokemonStats) {
    val labels = listOf("HP", "ATK", "DEF", "SPA", "SPD", "SPE")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Gráfico de Radar",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            RadarChart(
                stats = stats.toList(),
                labels = labels,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
        }
    }
}

@Composable
fun RadarChart(
    stats: List<Int>,           // Ej: [120, 95, 140, 70, 110, 80]
    labels: List<String>,       // Ej: ["HP","ATK","DEF","SP.ATK","SP.DEF","SPD"]
    modifier: Modifier = Modifier,
    steps: Int = 5              // Número de anillos
) {
    // ==========================
    // 🔥 Calcular maxValue automáticamente
    // ==========================
    val rawMax = stats.maxOrNull() ?: 1

    // Redondear a un número “bonito”
    val maxValue = when {
        rawMax <= 50  -> 50
        rawMax <= 75  -> 75
        rawMax <= 100 -> 100
        rawMax <= 150 -> 150
        rawMax <= 200 -> 200
        rawMax <= 250 -> 250
        rawMax <= 300 -> 300
        else          -> ((rawMax / 100f).toInt() + 1) * 100  // 400, 500, etc.
    }

    Canvas(modifier = modifier) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2.3f
        val angle = (2 * Math.PI / stats.size).toFloat()
        val stepRadius = radius / steps

        // ========= GRID (anillos)
        for (step in 1..steps) {
            val r = stepRadius * step
            val gridPath = Path().apply {
                for (i in stats.indices) {
                    val x = center.x + r * cos(angle * i)
                    val y = center.y + r * sin(angle * i)
                    if (i == 0) moveTo(x, y) else lineTo(x, y)
                }
                close()
            }

            drawPath(
                path = gridPath,
                color = Color.Black.copy(alpha = 0.35f),
                style = Stroke(width = 1.6f)
            )
        }

        // ========= Líneas radiales
        for (i in stats.indices) {
            val x = center.x + radius * cos(angle * i)
            val y = center.y + radius * sin(angle * i)
            drawLine(
                color = Color.Black.copy(alpha = 0.35f),
                start = center,
                end = Offset(x, y),
                strokeWidth = 1.4f
            )
        }

        // ========= Polígono
        val statPath = Path().apply {
            for (i in stats.indices) {
                val scaled = (stats[i] / maxValue.toFloat()) * radius
                val x = center.x + scaled * cos(angle * i)
                val y = center.y + scaled * sin(angle * i)
                if (i == 0) moveTo(x, y) else lineTo(x, y)
            }
            close()
        }

        drawPath(
            path = statPath,
            color = Color(0xFF2563EB).copy(alpha = 0.35f)
        )
        drawPath(
            path = statPath,
            color = Color(0xFF2563EB),
            style = Stroke(width = 3f)
        )

        // ========= Texto de escala (0 → maxValue)
//        val scalePaint = Paint().asFrameworkPaint().apply {
//            isAntiAlias = true
//            textSize = 28f
//            textAlign = android.graphics.Paint.Align.LEFT
//            color = android.graphics.Color.GRAY
//        }
//
//        for (step in 0..steps) {
//            val value = (maxValue / steps) * step
//            val r = stepRadius * step
//            val x = center.x + r + 6f
//            val y = center.y - 6f
//
//            drawContext.canvas.nativeCanvas.drawText(
//                value.toString(),
//                x,
//                y,
//                scalePaint
//            )
//        }

        // ========= Valor real de cada stat
        val valuePaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 30f
            textAlign = android.graphics.Paint.Align.CENTER
            color = android.graphics.Color.BLACK
        }

        stats.forEachIndexed { i, stat ->
            val scaled = (stat / maxValue.toFloat()) * radius
            val x = center.x + scaled * cos(angle * i)
            val y = center.y + scaled * sin(angle * i)

            drawContext.canvas.nativeCanvas.drawText(
                stat.toString(),
                x,
                y - 12,
                valuePaint
            )
        }

        // ========= Etiquetas
        val labelPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            textSize = 34f
            textAlign = android.graphics.Paint.Align.CENTER
            color = android.graphics.Color.DKGRAY
        }

        labels.forEachIndexed { i, label ->
            val labelRadius = radius + 45f
            val x = center.x + labelRadius * cos(angle * i)
            val y = center.y + labelRadius * sin(angle * i)

            drawContext.canvas.nativeCanvas.drawText(label, x, y, labelPaint)
        }
    }
}

/**
 * Placeholder para el Gráfico de Barras (Estadísticas Promedio)
 */
@Composable
fun BarChartCard(stats: TeamStats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Estadísticas Promedio",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Usamos un Box para simular el gráfico de barras
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                // Datos para el gráfico de barras: HP, ATK, DEF, SpA, SpD, SPE
                val barData = listOf(
                    Pair("HP", stats.hp),
                    Pair("ATK", stats.atk),
                    Pair("DEF", stats.def),
                    Pair("SpA", stats.spA),
                    Pair("SpD", stats.spD),
                    Pair("SPE", stats.spe)
                )
                SimpleBarChart(barData, maxValue = 150)
            }
        }
    }
}

/**
 * Implementación simplificada del Gráfico de Barras para el preview.
 * Esto representa la forma, pero no es una biblioteca de gráficos completa.
 */
@Composable
fun SimpleBarChart(barData: List<Pair<String, Int>>, maxValue: Int) {
    val barColor = Color(0xFF4285F4)
    val textColor = Color(0xFF6A6A6A)
    val barWidth = 24.dp
    val spacing = 12.dp

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.Bottom
    ) {
        barData.forEach { (label, value) ->
            val barHeightFraction = (value.toFloat() / maxValue.toFloat()).coerceIn(0f, 1f)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f) // Distribuye las barras uniformemente
            ) {
                // Etiqueta del valor superior (simplificada)
                // Text(value.toString(), fontSize = 10.sp, color = Color.Black)

                Spacer(modifier = Modifier.height(4.dp))

                // Barra
                Box(
                    modifier = Modifier
                        .height(150.dp) // Altura máxima para la zona de las barras
                        .width(barWidth)
                        .background(Color.Transparent)
                        .align(Alignment.CenterHorizontally)
                ) {
                    // La barra real
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(barHeightFraction)
                            .background(barColor)
                            .align(Alignment.BottomCenter)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Etiqueta de la estadística (HP, ATK, etc.)
                Text(label, fontSize = 12.sp, color = textColor)
            }
        }
    }
    // Eje Y simulado (Línea de 150)
    Canvas(Modifier.fillMaxSize()) {
        val yMax = size.height - 30.dp.toPx() // Ajustar al espacio de las barras
        drawLine(
            color = Color.LightGray,
            start = Offset(0f, yMax * (1 - 150f / maxValue)),
            end = Offset(size.width, yMax * (1 - 150f / maxValue)),
            strokeWidth = 1.dp.toPx()
        )
    }
}

/**
 * Tarjeta individual de una Estadística.
 */
@Composable
fun StatCard(statName: String, value: Int, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.8f), // Aproximadamente 1.8:1 para mantener la forma
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = statName,
                color = Color.White,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value.toString(),
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

/**
 * Componente para el mensaje de Distribución de Roles.
 */
@Composable
fun RoleDistributionCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Distribución de Roles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Asigna roles a tus Pokémon para ver la distribución",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

// --- 3. Vista Principal ---

@Composable
fun TeamStatsScreen() {
    val stats = sampleTeamStats
    // Definición de colores de las tarjetas
    val colors = listOf(
        Color(0xFFEF5350), // HP - Rojo
        Color(0xFFFFB300), // Defensa - Amarillo/Naranja
        Color(0xFF9CCC65), // ATK (No usado en tarjeta)
        Color(0xFF42A5F5), // At. Esp - Azul
        Color(0xFFAB47BC), // Def. Esp - Morado
        Color(0xFFEC407A)  // Velocidad - Rosa
    )

    // Mapeo específico para las 6 tarjetas que se ven
    val statCards = listOf(
        Triple("HP", stats.hp, colors[0]),
        Triple("Defensa", stats.def, colors[1]),
        Triple("Def. Esp", stats.spD, colors[4]),
        Triple("At. Esp", stats.spA, colors[3]),
        Triple("Velocidad", stats.spe, colors[5]),
        Triple("Ataque", stats.atk, colors[2]) // Usado para el placeholder de "Preview 60"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F0)) // Fondo ligeramente gris
    ) {
        // 1. Encabezado Azul
        item {
            StatsHeader(totalScore = stats.totalScore)
        }

        item {
            Column(modifier = Modifier.padding(16.dp)) {
                // 2. Gráfico de Radar
                val charizardStats = PokemonStats(
                    hp = 78,
                    attack = 84,
                    defense = 78,
                    specialAttack = 109,
                    specialDefense = 85,
                    speed = 100
                )

                RadarChartCard(stats = charizardStats)
                // 3. Gráfico de Barras
                BarChartCard(stats = stats)

                // 4. Tarjetas de Estadísticas Individuales
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Columna 1 (HP, Defensa, Def. Esp)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(statCards[0].first, statCards[0].second, statCards[0].third)
                        StatCard(statCards[2].first, statCards[2].second, statCards[2].third)
                        StatCard(statCards[4].first, statCards[4].second, statCards[4].third)
                    }

                    // Columna 2 (At. Esp, Velocidad, Ataque)
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(statCards[3].first, statCards[3].second, statCards[3].third)
                        StatCard(statCards[5].first, statCards[5].second, statCards[5].third)
                        // Para simular la tarjeta de Ataque (ATK) que está en la posición 2 de la imagen
                        StatCard(statCards[1].first, statCards[1].second, statCards[1].third)
                    }
                }

                // 5. Distribución de Roles
                RoleDistributionCard()
            }
        }
    }
}


data class TeamStats(
    val totalScore: Int,
    val hp: Int,
    val atk: Int,
    val def: Int,
    val spA: Int,
    val spD: Int,
    val spe: Int
)

val sampleTeamStats = TeamStats(
    totalScore = 386,
    hp = 57,
    atk = 60,
    def = 61,
    spA = 71,
    spD = 70,
    spe = 67
)

val statCardData = listOf(
    Pair("HP", sampleTeamStats.hp),
    Pair("Ataque", sampleTeamStats.atk),
    Pair("Defensa", sampleTeamStats.def),
    Pair("At. Esp", sampleTeamStats.spA),
    Pair("Def. Esp", sampleTeamStats.spD),
    Pair("Velocidad", sampleTeamStats.spe)
)

