package com.cavies.pokedex.presentation.theme

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun PokeDexTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val targetColors = if (darkTheme) DarkColorScheme else LightColorScheme

    UpdateSystemTheme(
        darkTheme = darkTheme,
        targetColor = targetColors.background
    )

    val animatedBackground by animateColorAsState(targetColors.background, label = "bg")
    val animatedSurface by animateColorAsState(targetColors.surface, label = "surface")
    val animatedPrimary by animateColorAsState(targetColors.primary, label = "primary")
    val animatedOnBackground by animateColorAsState(targetColors.onBackground, label = "onBg")
    val animatedOnPrimary by animateColorAsState(targetColors.onPrimary, label = "onPrimary")

    val colors = targetColors.copy(
        background = animatedBackground,
        surface = animatedSurface,
        primary = animatedPrimary,
        onBackground = animatedOnBackground,
        onPrimary = animatedOnPrimary
    )

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        shapes = Shapes(
            small = RoundedCornerShape(8.dp),
            medium = RoundedCornerShape(16.dp),
            large = RoundedCornerShape(24.dp)
        ),
        content = content
    )
}

@Composable
fun UpdateSystemTheme(
    darkTheme: Boolean,
    targetColor: Color
) {
    val context = LocalContext.current
    val activity = context.findComponentActivity() ?: return

    val animatedColor = animateColorAsState(targetValue = targetColor, label = "systemBarColor")

    SideEffect {
        val colorInt = animatedColor.value.toArgb()

        activity.enableEdgeToEdge(
            statusBarStyle = if (darkTheme) {
                SystemBarStyle.dark(colorInt)
            } else {
                SystemBarStyle.light(colorInt, colorInt)
            },
            navigationBarStyle = if (darkTheme) {
                SystemBarStyle.dark(colorInt)
            } else {
                SystemBarStyle.light(colorInt, colorInt)
            }
        )
    }
}

fun Context.findComponentActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.findComponentActivity()
    else -> null
}
