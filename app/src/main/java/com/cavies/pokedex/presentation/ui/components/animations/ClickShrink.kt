package com.cavies.pokedex.presentation.ui.components.animations

import android.view.MotionEvent
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun StaggeredItemAnimation(
    index: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val delay = (index * 50).coerceAtMost(400) // máx 400 ms

    val alpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(300, delayMillis = delay),
        label = "alpha"
    )

    val offsetY by animateDpAsState(
        targetValue = 0.dp,
        animationSpec = tween(350, delayMillis = delay, easing = LinearOutSlowInEasing),
        label = "offsetY"
    )

    Box(
        modifier = modifier
            .graphicsLayer { this.alpha = alpha }
            .offset(y = offsetY + 12.dp) // aparece desde abajo
    ) {
        content()
    }
}




fun Modifier.shrinkClick(
    scaleDown: Float = 0.92f,
    duration: Int = 120,
    onClick: () -> Unit
): Modifier = composed {
    var pressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val scale by animateFloatAsState(
        targetValue = if (pressed) scaleDown else 1f,
        animationSpec = tween(durationMillis = duration, easing = LinearOutSlowInEasing),
        label = "shrinkTap"
    )

    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pointerInteropFilter {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    pressed = true
                }
                MotionEvent.ACTION_UP -> {
                    scope.launch {
                        delay(duration.toLong())
                        pressed = false
                        onClick()
                    }
                }
                MotionEvent.ACTION_CANCEL -> {
                    pressed = false
                }
            }
            true
        }
}
