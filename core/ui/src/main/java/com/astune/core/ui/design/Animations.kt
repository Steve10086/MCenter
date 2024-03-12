package com.astune.core.ui.design

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp




@Composable
fun LoadingAnimation(
    size: Dp = 250.dp,
) {
    var state by remember { mutableStateOf(true) }
    val infiniteTransition = updateTransition(state, label = "loading")

    val animateOffset1 by infiniteTransition.animateFloat(
        transitionSpec = {
            return@animateFloat repeatable(
            animation = keyframes {
                durationMillis = 800
                0f at 0 with EaseIn
                1f at 800 with EaseIn
            },
            iterations = 2,
            repeatMode = RepeatMode.Reverse)},
        label = ""
    ) {state ->
        if (state) 0f else 0.0000001f
        }

    val animateOffset2 by infiniteTransition.animateFloat(
        transitionSpec = {
            return@animateFloat repeatable(
                animation = keyframes {
                    durationMillis = 800
                    delayMillis = 400
                    0f at 0 with EaseIn
                    1f at 800 with EaseIn
                },
                iterations = 2,
                repeatMode = RepeatMode.Reverse) },
        label = ""
    ) {state ->
        if (state) 0f else 0.0000001f
    }

    val animateOffset3 by infiniteTransition.animateFloat(
        transitionSpec = {
            return@animateFloat repeatable(
                animation = keyframes {
                    durationMillis = 800
                    delayMillis = 800
                    0f at 0 with EaseIn
                    1f at 800 with EaseIn
                },
                iterations = 2,
                repeatMode = RepeatMode.Reverse,)
                },
        label = ""
    ) {state ->
        if (state) 0f else 0.0000001f
    }

    
    val width = size * 0.125f
    val baseHeight = size * 0.25f
    val fra1 = 2f
    val fra2 = fra1 + 1f

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(size)) {
            drawRect(
                color = Color.Gray,
                size = Size(width = width.toPx(),
                    height = baseHeight.toPx()
                ),
                topLeft = Offset(
                    x = width.toPx() * 3/2,
                    y = (size - baseHeight).toPx() - baseHeight.toPx() * animateOffset1))

            drawRect(
                color = Color.Gray,
                size = Size(
                    width = width.toPx(),
                    height = baseHeight.toPx() * fra1
                ),
                topLeft = Offset(
                    x = width.toPx() * 7/2,
                    y = (size - baseHeight * fra1).toPx() - baseHeight.toPx() * animateOffset2))

            drawRect(
                color = Color.Gray,
                size = Size(
                    width = width.toPx(),
                    height = baseHeight.toPx() * fra2),
                topLeft = Offset(
                    x = width.toPx() * 11/2,
                    y = (size - baseHeight * fra2).toPx() - baseHeight.toPx() * animateOffset3))

            drawRect(color = Color.Red, size = Size(width = width.toPx(), height = width.toPx()), topLeft = Offset(x = 0f, y = size.toPx() * 0.875f))
        }
    }
}

@Preview
@Composable
fun LoadingAnimationPreview(){
    Surface(
        modifier = Modifier.size(250.dp),
        color = Color.White
        ){
        LoadingAnimation()
    }

}