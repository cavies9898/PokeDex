package com.cavies.pokedex.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cavies.pokedex.presentation.ui.home.HomeScreen
import com.cavies.pokedex.presentation.ui.splash.SplashScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    navManager: NavigationManager = hiltViewModel<NavHostViewModel>().navManager
) {
    LaunchedEffect(Unit) {
        navManager.commands.collect { command ->
            when (command) {
                is NavigationCommand.NavigateTo -> {
                    navController.navigate(command.route) {
                        launchSingleTop = command.singleTop
                    }
                }
                is NavigationCommand.NavigateAndPop -> {
                    navController.navigate(command.route) {
                        popUpTo(command.popUpTo) { inclusive = command.inclusive }
                    }
                }
                NavigationCommand.NavigateUp -> navController.navigateUp()
                NavigationCommand.PopBack -> navController.popBackStack()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) { SplashScreen() }
        composable(Routes.HOME) { HomeScreen() }
    }
}
