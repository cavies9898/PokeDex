package com.cavies.pokedex.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cavies.pokedex.presentation.ui.favorite.FavoritesScreen
import com.cavies.pokedex.presentation.ui.home.HomeScreen
import com.cavies.pokedex.presentation.ui.splash.SplashScreen


@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val navVM: NavHostViewModel = hiltViewModel()
    val navManager = navVM.navManager

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
                NavigationCommand.PopBack -> navController.popBackStack()
                NavigationCommand.NavigateUp -> navController.navigateUp()
            }
        }
    }

    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) { SplashScreen() }
        composable(Routes.HOME) { HomeScreen() }
//        composable(Routes.EQUIPO) { TeamScreen() }
        composable(Routes.FAVORITES) { FavoritesScreen() }
//        composable(Routes.SETTINGS) { SettingsScreen() }

//        composable("${Routes.DETAIL}/{id}") {
//            val id = it.arguments?.getString("id")!!
//            DetailScreen(id)
//        }
    }
}

