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
import com.cavies.pokedex.presentation.navigation.Favorites
import com.cavies.pokedex.presentation.navigation.Home
import com.cavies.pokedex.presentation.navigation.Settings
import com.cavies.pokedex.presentation.navigation.Team

val bottomBarItems = listOf(
    BottomItem(
        route = Home,
        title = "Inicio",
        icon = Icons.Outlined.Home,
        iconSelected = Icons.Filled.Home
    ),
    BottomItem(
        route = Team,
        title = "Equipo",
        icon = Icons.Outlined.Group,
        iconSelected = Icons.Filled.Group
    ),
    BottomItem(
        route = Favorites,
        title = "Favoritos",
        icon = Icons.Outlined.FavoriteBorder,
        iconSelected = Icons.Filled.Favorite
    ),
    BottomItem(
        route = Settings,
        title = "Ajustes",
        icon = Icons.Outlined.Settings,
        iconSelected = Icons.Filled.Settings
    )
)