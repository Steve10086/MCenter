package com.astune.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class TitleBarValues (
    var rightButtonImg: ImageVector,
    var leftButtonImg: ImageVector,
    var title: String = "title",
    var enableLeft: Boolean = true,
    var enableRight: Boolean = true,
){
    Default(
        rightButtonImg = Icons.Default.Add,
        leftButtonImg= Icons.Default.Add,
        ),

    DEVICE(
        rightButtonImg = Icons.Default.Add,
        leftButtonImg= Icons.Default.Info,
        title = "devices",
        enableLeft = true,
        enableRight = true,
    ),

    SETTING(
        rightButtonImg = Icons.Default.Done,
        leftButtonImg= Icons.Default.Close,
        title = "settings",
        enableLeft = true,
        enableRight = true,
    ),

    LINK(
        rightButtonImg = Icons.Default.Add,
        leftButtonImg= Icons.Default.Close,
        title = "links",
        enableLeft = true,
        enableRight = false,
    ),

    SIDE_BAR(
        rightButtonImg = Icons.Default.Add,
        leftButtonImg= Icons.Default.Edit,
        title = "devices",
        enableLeft = true,
        enableRight = false,
    )
}