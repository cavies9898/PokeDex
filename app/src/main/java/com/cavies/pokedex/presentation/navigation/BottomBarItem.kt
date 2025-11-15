package com.cavies.pokedex.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomBarItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val iconSelected: ImageVector
)