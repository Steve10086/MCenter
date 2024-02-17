package com.astune.core.ui


enum class CustomKey(
    val id: String,
    val text: String
){

    Esc(
        id = "esc",
        text = "esc"
    ),

    Tab(
        id = "tab",
        text = "tab",
    ),

    Ctrl(
        id = "ctrl",
        text = "ctrl",
    ),

    Alt(
        id = "alt",
        text = "alt"
    ),

    Up(
        id = "up",
        text = "▲"
    ),

    Down(
        id = "down",
        text = "▼"
    ),

    Left(
        id = "left",
        text = "◀"
    ),

    Right(
        id = "right",
        text = "▶"
    ),
}

enum class CustomKeyLists(
    val keyList:List<CustomKey>
){

    Shell(
        keyList = listOf(
            CustomKey.Esc,
            CustomKey.Tab,
            CustomKey.Ctrl,
            CustomKey.Alt,
            CustomKey.Up,
            CustomKey.Down,
            CustomKey.Left,
            CustomKey.Right
        )
    )
}
