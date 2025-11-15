package com.cavies.pokedex.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cavies.pokedex.presentation.navigation.AppNavHost
import com.cavies.pokedex.presentation.components.CustomBottomBar
import com.cavies.pokedex.presentation.components.CustomTopBar
import com.cavies.pokedex.presentation.navigation.Routes

@Composable
fun AppScreen(
    navController: NavHostController
) {
    val currentDestination = navController
        .currentBackStackEntryAsState()
        .value?.destination?.route

    val showBars = currentDestination !in listOf(Routes.SPLASH)

    Scaffold(
        topBar = { if (showBars) CustomTopBar() },
        bottomBar = { if (showBars) CustomBottomBar(navController) }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            AppNavHost(navController)
        }
    }
}