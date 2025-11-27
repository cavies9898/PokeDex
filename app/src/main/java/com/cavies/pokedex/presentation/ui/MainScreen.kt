package com.cavies.pokedex.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cavies.pokedex.presentation.ui.components.menu.CustomBottomBar
import com.cavies.pokedex.presentation.navigation.AppNavHost
import com.cavies.pokedex.presentation.navigation.Routes

@Composable
fun MainScreen(
    navController: NavHostController
) {
    val currentDestination = navController
        .currentBackStackEntryAsState()
        .value?.destination?.route

    val showBars = currentDestination !in listOf(Routes.SPLASH)

    Column(modifier = Modifier.fillMaxSize()) {
        AppNavHost(
            navController = navController,
            modifier = Modifier.weight(1f)
        )

        if (showBars) CustomBottomBar(navController)
    }
}