package com.cavies.pokedex.presentation.navigation

sealed class NavigationCommand {
    data class NavigateTo(
        val route: String,
        val singleTop: Boolean = false
    ) : NavigationCommand()

    data class NavigateAndPop(
        val route: String,
        val popUpTo: String,
        val inclusive: Boolean = false
    ) : NavigationCommand()

    object NavigateUp : NavigationCommand()
    object PopBack : NavigationCommand()
}
