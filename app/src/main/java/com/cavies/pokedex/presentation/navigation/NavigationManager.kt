package com.cavies.pokedex.presentation.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cavies.pokedex.presentation.ui.components.menu.BottomBarNavigation
import com.cavies.pokedex.presentation.ui.favorite.FavoritesScreen
import com.cavies.pokedex.presentation.ui.home.HomeScreen
import com.cavies.pokedex.presentation.ui.settings.SettingsScreen
import com.cavies.pokedex.presentation.ui.splash.SplashScreen
import com.cavies.pokedex.presentation.ui.team.TeamScreen
import com.cavies.pokedex.presentation.ui.team.stack.TeamStackView

private val bottomBarRoutes = setOf(
    Home::class.qualifiedName,
    Team::class.qualifiedName,
    Favorites::class.qualifiedName,
    Settings::class.qualifiedName,
)
@Composable
fun NavigationManager() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    val showBottomBar = bottomBarRoutes.contains(currentRoute)

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        bottomBar = { if (showBottomBar) BottomBarNavigation(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Splash,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            composable<Splash> {
                SplashScreen {
                    navController.navigate(Home) {
                        popUpTo(Splash) { inclusive = true }
                    }
                }
            }

            composable<Home> { HomeScreen() }

            /**
             * Team Stack
             * */
            composable<Team> {
                TeamScreen {
                    navController.navigate(TeamStack) {
                        launchSingleTop = true
                    }
                }
            }
            composable<TeamStack> {
                TeamStackView(
                    onBackPressed = { navController.popBackStack() },
                )
            }
            /***/

            composable<Favorites> { FavoritesScreen() }
            composable<Settings> { SettingsScreen() }
        }
    }
}

