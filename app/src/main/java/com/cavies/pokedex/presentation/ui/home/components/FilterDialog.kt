package com.cavies.pokedex.presentation.ui.home.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlin.math.min


@Composable
fun FilterDialog(
    onDismiss: () -> Unit,
    onFilterSelected: (item: FilterItem) -> Unit
) {
    val items = FilterItem.entries.toList()
    val radius = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        radius.animateTo(
            targetValue = 1800f,
            animationSpec = tween(420)
        )
    }

    val pageCount = (items.size + 8) / 9
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f,
        pageCount = { pageCount }
    )

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawCircle(
                        color = Color.Black.copy(alpha = 0.75f),
                        radius = radius.value
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1E)),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .width(330.dp)
                    .wrapContentHeight()
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color.White.copy(alpha = 0.15f), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cerrar",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Selecciona un filtro",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        repeat(pageCount) { index ->
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (pagerState.currentPage == index) Color.White
                                        else Color.White.copy(alpha = 0.3f)
                                    )
                            )
                        }
                    }

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.height(300.dp)
                    ) { page ->
                        val start = page * 9
                        val end = min(start + 9, items.size)
                        val pageItems = items.subList(start, end)

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            verticalArrangement = Arrangement.spacedBy(18.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(pageItems) { item ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .width(80.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color.White.copy(alpha = 0.12f))
                                        .clickable { onFilterSelected(item) }
                                        .padding(9.dp)
                                ) {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(26.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = item.displayName,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 8.sp,
                                        textAlign = TextAlign.Center,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FilterBottomSheet(
    optionsList: List<String>,
    selectedTypes: Set<String>,
    onApply: (selectedOption: List<String>) -> Unit
) {
    val allSelected = remember {
        mutableStateListOf<String>().apply { addAll(selectedTypes) }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            "Filtrar Pokémon",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text("Tipo", fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            optionsList.forEach { type ->

                val isSelected = allSelected.contains(type)

                FilterChip(
                    selected = isSelected,
                    onClick = {
                        if (isSelected) {
                            allSelected.remove(type)
                        } else {
                            allSelected.add(type)
                        }
                    },
                    label = { Text(type.replaceFirstChar { it.uppercase() }) }
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { onApply(allSelected) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Aplicar filtros")
        }
    }
}

