package com.cavies.pokedex.presentation.navigation

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationManager {
    private val _commands = MutableSharedFlow<NavigationCommand>()
    val commands = _commands.asSharedFlow()

    suspend fun navigate(command: NavigationCommand) {
        _commands.emit(command)
    }

    suspend fun navigateTo(route: String, singleTop: Boolean = false) {
        _commands.emit(NavigationCommand.NavigateTo(route, singleTop))
    }

    suspend fun navigateAndPop(route: String, popUpTo: String, inclusive: Boolean = false) {
        _commands.emit(NavigationCommand.NavigateAndPop(route, popUpTo, inclusive))
    }

    suspend fun navigateUp() {
        _commands.emit(NavigationCommand.NavigateUp)
    }

    suspend fun popBack() {
        _commands.emit(NavigationCommand.PopBack)
    }
}
