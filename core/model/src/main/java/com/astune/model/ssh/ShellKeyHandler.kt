package com.astune.model.ssh

enum class CursorState(
    val value:Int
){
    DEFAULT(
        value = 0
    ),

    APPLICATION(
        value = 1
    )
}
enum class InputState(
    val value: Int,
    val initState:Boolean
){
    /*using escO instead of esc[ as commend header*/
    DECCKM(
        value = 1,
        initState = false
    ),

    DECCOLM(
        value = 3,
        initState = false
    ),

    DECSCNM(
        value = 5,
        initState = false
    ),

    DECOM(
        value = 6,
        initState = false
    ),

    /*Auto-warp*/
    DECAWM(
        value = 7,
        initState = true
    ),

    /*Keyboard auto-repeat*/
    DECARM(
        value = 8,
        initState = true
    ),

    X10_MOUSE(
        value = 9,
        initState = false
    ),

    /*Cursor visibility*/
    DECTECH(
        value = 25,
        initState = true
    ),

    X11_MOUSE(
        value = 1000,
        initState = false
    ),


}

enum class Action(
    var param: ShellFunctionParam = ShellFunctionParam(emptyList())
){
    CHANGE_STYLE,
    MOVE_CURSOR,
    MOVE_CURSOR_REVERSE,
    MOVE_CURSOR_ABS,
    DELETE,
    DELETE_LINE,
    STORE_CURSOR,
    RESTORE_CURSOR,
    SCROLL,
    SCROLL_REVERSE,
    RESTRICT_BOUNDS,
    SET_CURSOR_STATE,
    DEC_MODE_ON,
    DEC_MODE_OFF,
    SET_ECMA_48_MODE,
    SET_PASTE,
}

//TODO:remove size attribute
data class ShellFunctionParam(
    var params:MutableList<Int>,
    var size:Int = params.size

){
    companion object{
        const val INVALID_PARAM = -2
        const val NEGATIVE_INF = -3
        //const val INVALID_PARAM = -2
        val SPECIAL_PARAM = listOf(INVALID_PARAM, NEGATIVE_INF)
    }

    operator fun get(i: Int): Int {
        return params[i]
    }
}
fun ShellFunctionParam(params: List<Int>): ShellFunctionParam {
    return ShellFunctionParam(params = params.toMutableList())
}

