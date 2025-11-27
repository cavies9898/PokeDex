package com.cavies.pokedex.presentation.ui.components.menu

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import com.cavies.pokedex.presentation.navigation.Routes

val bottomBarItems = listOf(
    BottomBarItem(
        route = Routes.HOME,
        title = "Inicio",
        icon = Icons.Outlined.Home,
        iconSelected = Icons.Filled.Home
    ),
    BottomBarItem(
        route = Routes.TEAM,
        title = "Equipo",
        icon = Icons.Outlined.Group,
        iconSelected = Icons.Filled.Group
    ),
    BottomBarItem(
        route = Routes.FAVORITES,
        title = "Favoritos",
        icon = Icons.Outlined.FavoriteBorder,
        iconSelected = Icons.Filled.Favorite
    ),
    BottomBarItem(
        route = Routes.SETTINGS,
        title = "Ajustes",
        icon = Icons.Outlined.Settings,
        iconSelected = Icons.Filled.Settings
    )
)