package com.astune.core.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap

data class UIState(
    var background:ImageBitmap = ImageBitmap(1, 1),
    var screenSize: Size = Size.Unspecified,
    var positionInRoot: Offset = Offset.Zero,
    var rightActions: MutableMap<String, () -> Unit> = mutableMapOf(),
    var leftActions: MutableMap<String, () -> Unit> = mutableMapOf(),

    val rightBtnClicked: () -> Unit = {
        for (action in rightActions){
            action.value.invoke()
        }
    },

    val leftBtnClicked: () -> Unit = {
        for (action in leftActions.values){
            action.invoke()
        }
    },
)

fun UIState.setOnRightBtnClicked(key:String, action: () -> Unit){
    rightActions[key] = action
}
fun UIState.merge(uiState: UIState):UIState{
    background = uiState.background
    screenSize = uiState.screenSize
    positionInRoot = uiState.positionInRoot
    rightActions += uiState.rightActions
    return this
}

val LocalRootUIState = staticCompositionLocalOf { UIState() }
