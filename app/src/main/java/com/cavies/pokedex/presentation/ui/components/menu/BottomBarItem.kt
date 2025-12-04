package com.cavies.pokedex.presentation.ui.components.menu

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomItem(
    val title: String,
    val icon: ImageVector,
    val iconSelected: ImageVector,
    val route: Any
)