package com.astune.core.ui.design

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class GradientColors(
    val top: Color = Color.Unspecified,
    val bottom: Color = Color.Unspecified,
    val container: Color = Color.Unspecified,
)

@Composable
fun defaultGradientColor() =
    GradientColors(
        top = MaterialTheme.colorScheme.inverseOnSurface,
        bottom = MaterialTheme.colorScheme.secondaryContainer,
        container = MaterialTheme.colorScheme.surface,
    )

@Composable
fun halfTransparentColor(color : Color) =
    GradientColors(
        top = color,
        bottom = Color.Transparent,
        container = MaterialTheme.colorScheme.surface
    )