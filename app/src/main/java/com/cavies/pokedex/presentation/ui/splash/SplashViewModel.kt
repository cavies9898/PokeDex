package com.cavies.pokedex.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cavies.pokedex.presentation.navigation.NavigationManager
import com.cavies.pokedex.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val navManager: NavigationManager
): ViewModel() {

    fun onSplashFinished() {
        viewModelScope.launch {
            navManager.navigateAndPop(Routes.HOME, Routes.SPLASH, inclusive = true)
        }
    }
}
