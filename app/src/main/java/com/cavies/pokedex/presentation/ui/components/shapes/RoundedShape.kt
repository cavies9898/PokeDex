package com.cavies.pokedex.presentation.ui.components.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class RoundedShape(
    private val bottomRadius: Float = 60f
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val path = Path().apply {

            moveTo(0f, 0f)
            lineTo(0f, size.height - bottomRadius)

            quadraticTo(
                0f,
                size.height,
                bottomRadius,
                size.height
            )

            lineTo(size.width - bottomRadius, size.height)

            quadraticTo(
                size.width,
                size.height,
                size.width,
                size.height - bottomRadius
            )

            lineTo(size.width, 0f)

            close()
        }

        return Outline.Generic(path)
    }
}