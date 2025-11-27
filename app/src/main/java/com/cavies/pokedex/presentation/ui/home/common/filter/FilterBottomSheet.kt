package com.cavies.pokedex.presentation.ui.home.common.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


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
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text("Aplicar filtros")
        }

    }
}