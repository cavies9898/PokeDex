package com.cavies.pokedex.presentation.ui.components.menu

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cavies.pokedex.presentation.navigation.Routes


@Composable
fun CustomBottomBar(navController: NavHostController) {

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        bottomBarItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(Routes.HOME) { saveState = true }
                        }
                    }
                },
                label = { Text(item.title) },
                icon = {
                    Icon(
                        imageVector = if (currentRoute == item.route) item.iconSelected else item.icon,
                        contentDescription = item.title
                    )
                }
            )
        }
    }
}

