package com.astune.core.ui.design

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

class RoundedCornerRectangleShape(
    private val cornerRadius: Dp
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rounded(
            RoundRect(
                left = 0f,
                top = 0f,
                right = size.width,
                bottom = size.height,
                topLeftCornerRadius = CornerRadius(cornerRadius.value, cornerRadius.value),
                topRightCornerRadius = CornerRadius(cornerRadius.value, cornerRadius.value),
                bottomLeftCornerRadius = CornerRadius(cornerRadius.value, cornerRadius.value),
                bottomRightCornerRadius = CornerRadius(cornerRadius.value, cornerRadius.value),
            ),
        )
    }
}