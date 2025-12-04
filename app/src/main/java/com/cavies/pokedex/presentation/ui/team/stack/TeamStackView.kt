package com.cavies.pokedex.presentation.ui.team.stack

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cavies.pokedex.presentation.navigation.TeamScreens
import com.cavies.pokedex.presentation.ui.team.stack.analysis.TeamAnalysisScreen
import com.cavies.pokedex.presentation.ui.team.stack.builder.TeamBuilderHeader
import com.cavies.pokedex.presentation.ui.team.stack.builder.TeamBuilderScreen
import com.cavies.pokedex.presentation.ui.team.stack.coverage.TeamCoverageScreen
import com.cavies.pokedex.presentation.ui.team.stack.stats.TeamStatsScreen

@Composable
fun TeamStackView(
    onBackPressed: () -> Unit,
) {
    var screenSelect by remember { mutableStateOf(TeamScreens.TEAM) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F0F8))
    ) {
        TeamBuilderHeader(
            onBackClick = { onBackPressed() },
            onScreenChanged = { screenSelect = it }
        )

        AnimatedContent(
            targetState = screenSelect,
            transitionSpec = {
                slideInHorizontally() + fadeIn() togetherWith slideOutHorizontally() + fadeOut()
            },
            label = "TeamScreenAnimation"
        ) { screen ->
            when (screen) {
                TeamScreens.TEAM -> TeamBuilderScreen()
                TeamScreens.STATS -> TeamStatsScreen()
                TeamScreens.COVERAGE -> TeamCoverageScreen()
                TeamScreens.ANALYSIS -> TeamAnalysisScreen()
            }
        }
    }
}
