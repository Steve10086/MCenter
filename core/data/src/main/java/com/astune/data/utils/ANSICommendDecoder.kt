package com.astune.data.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.astune.model.ssh.Action
import com.astune.model.ssh.ShellContent
import com.astune.model.ssh.ShellFunctionParam
import com.astune.model.ssh.ShellFunctionParam.Companion.NEGATIVE_INF
import kotlin.math.max


internal fun decodeEscapeSeries(commend:String):Action?{
    var params = commend.dropLast(1).split(";").map{
        return@map if (it == "" || it.contains("?")) 0 else it.toInt()
    }.toMutableList()
    val action:Action?
    when(commend.last()){
        //color
        'm' -> {
            action = Action.CHANGE_STYLE
            action.param = ShellFunctionParam(params) //reformat
        }
        //movement
        'A' -> {
            action = Action.MOVE_CURSOR_REVERSE
            params = params.padEnd(2, 0)
            action.param = ShellFunctionParam(
                params.apply {
                    params[1] = params[0]
                    params[0] = 0
                }
            )
        }
        'B' -> {
            action = Action.MOVE_CURSOR
            params = params.padEnd(2, 0)
            action.param = ShellFunctionParam(
                params.apply {
                    params = params.padEnd(2, 0)
                    params[1] = params[0]
                    params[0] = 0
                }
            )
        }
        'C' -> {
            action = Action.MOVE_CURSOR
            params = params.padEnd(2, 0)
            action.param = ShellFunctionParam(
                params.apply {
                    params = params.padEnd(2, 0)
                    params[1] = 0
                    params[0] = params[0].takeIf { it != 0 } ?:1
                }
            )
        }
        'D' -> {
            action = Action.MOVE_CURSOR_REVERSE
            params = params.padEnd(2, 0)
            action.param = ShellFunctionParam(
                params.apply {
                    params = params.padEnd(2, 0)
                    params[1] = 0
                    params[0] = -(params[0].takeIf { it != 0 } ?:1)
                }
            )
        }
        'E' -> {
            action = Action.MOVE_CURSOR
            params = params.padEnd(2, 0)
            action.param = ShellFunctionParam(
                params.apply {
                    params[0] = NEGATIVE_INF
                    params[1] = params[0].takeIf { it != 0 } ?:1
                }
            )
        }
        'F' -> {
            action = Action.MOVE_CURSOR_REVERSE
            params = params.padEnd(2, 0)
            action.param = ShellFunctionParam(
                params = params.apply{
                    params[0] = NEGATIVE_INF
                    params[1] = params[0].takeIf { it != 0 } ?:1
                }
            )
        }
        'f','G' -> {
            action = Action.MOVE_CURSOR_ABS
            params = params.padEnd(2, 0)
            action.param = ShellFunctionParam(
                params.apply {
                    params[0] -= 1
                    params[1] = NEGATIVE_INF
                }
            )
        }
        'H' -> {
            action = Action.MOVE_CURSOR_ABS
            params = params.padEnd(2, 0)
            action.param = ShellFunctionParam(
                params = params.apply {
                    with(params[0]){
                        params[0] = params[1] - 1
                        params[1] = this.zeroOrMinusOne()
                    }

                }
            )
        }

        //deletion
        'J' -> {
            action = Action.DELETE_LINE
            action.param = ShellFunctionParam(
                params.padEnd(1, 0)
            )
        }
        'K' -> {
            action = Action.DELETE
            action.param = ShellFunctionParam(
                params.padEnd(1, 0)
            )
        }
        //scroll
        'r' -> {
            action = Action.RESTRICT_BOUNDS
            action.param = ShellFunctionParam(
                params.padEnd(2, 0)
            )
        }
        'S' -> {
            action = Action.SCROLL_REVERSE
            params = params.padEnd(1, 0)
            action.param = ShellFunctionParam(
                params.apply {
                    params[0] = params[0].takeIf { it != 0 } ?:1
                }
            )
        }
        'T' -> {
            action = Action.SCROLL
            params = params.padEnd(1, 0)
            action.param = ShellFunctionParam(
                params.apply {
                    params[0] = params[0].takeIf { it != 0 } ?:1
                }
            )
        }
        //pointer
        's' -> action = Action.STORE_CURSOR
        'u' -> action = Action.RESTORE_CURSOR
        'h' -> {
            action = if(commend.contains("?")){
                Action.DEC_MODE_ON
            }else{
                Action.SET_ECMA_48_MODE
            }

            action.param = ShellFunctionParam(
                mutableListOf(commend.dropLast(1).drop(1).toIntOrNull()?:0)
            )
        }
        'l' -> {
            action = if(commend.contains("?")){
                Action.DEC_MODE_OFF
            }else{
                Action.SET_ECMA_48_MODE
            }

            action.param = ShellFunctionParam(
                mutableListOf(commend.dropLast(1).drop(1).toIntOrNull()?:0)
            )
        }
        '=' -> action = Action.SET_CURSOR_STATE
        '>' -> action = Action.SET_CURSOR_STATE
        else -> action = null
    }
    return action
}

internal fun getPureText(text: String):String{
    val string = StringBuilder()
    text.dropWhile {
        string.append(it)
        it != '\u001b'
    }
    return string.removeSuffix("\u001b").toString()
}

fun execMovePointerAbs(content: ShellContent, x:Int? = null, y:Int? = null){
    content.movePointerAbs(x?.minus(1), y?.zeroOrMinusOne()?.plus(content.bounds.first))
}

fun execDeleteLine(content: ShellContent, commend:Int){
    when(commend){
        0 -> {
            content.delete(content.pointer.second + 1..content.bounds.second)
            execDelete(content, 0)
        }
        1 -> content.delete(content.bounds.first..content.pointer.second)
        2 -> content.delete(content.bounds.first..content.bounds.second)
        3 -> content.delete(0 until content.content.size)
    }
}

fun execDelete(content: ShellContent, commend:Int){
    when(commend){
        0 -> content.deleteLine(content.pointer.second, content.pointer.first + 1..content.currentLineLength() + 1)
        1 -> content.deleteLine(content.pointer.second, 0..content.pointer.first)
        2 -> content.delete(content.pointer.second..content.pointer.second)
    }
}

fun execScroll(content: ShellContent, commend:Int){
    content.moveBounds(content.bounds.first + commend, content.bounds.second + commend)
}

fun rendText(text:String, style: SpanStyle): AnnotatedString{
    return buildAnnotatedString {
        withStyle(
            style = style
        ){
            append(text)
        }
    }
}


fun ANSIToStyle(
    params:ShellFunctionParam,
    style: SpanStyle = SpanStyle()
):SpanStyle{
    when (params.size){
        1 -> return singleParamStyles(params[0], style)
        3 -> return tripleParamStyles(params, style)
        5 -> return rgbStyles(params, style)
    }
    return style
}

fun singleParamStyles(
    value:Int,
    style: SpanStyle
):SpanStyle{
    return when (value){
        0 -> SpanStyle()
        1 -> style.copy(fontWeight = FontWeight.Bold)
        3 -> style.copy(fontStyle = FontStyle.Italic)
        4 -> style.copy(textDecoration = TextDecoration.Underline)
        in 30..37 -> style.copy(color = colorPlate(value - 30))
        in 40..47 -> style.copy(background = colorPlate(value - 40))
        in 90 .. 97 -> style.copy(color = colorPlate(value - 30).apply {
            this + Color(50, 50, 50, 0)
        })
        in 100..107 -> style.copy(background = colorPlate(value - 30).apply {
            this + Color(50, 50, 50, 0)
        })

        else -> SpanStyle()
    }
}


internal fun tripleParamStyles(
    values:ShellFunctionParam,
    style: SpanStyle
): SpanStyle {
    return when(values[0]){
        38 -> style.copy(color = Color(values[2]))
        48 -> style.copy(background = Color(values[2]))
        else -> {style}
    }
}

internal fun rgbStyles(
    values:ShellFunctionParam,
    style: SpanStyle
): SpanStyle{
    return when(values[0]){
        38 -> style.copy(color = Color(values[2],values[3],values[4]))
        48 -> style.copy(background = Color(values[2],values[3],values[4]))
        else -> {style}
    }
}

//default colors in shell
internal fun colorPlate(color:Int): Color {
    return when (color){
        0 -> Color.Black
        1 -> Color.Red
        2 -> Color.Green
        3 -> Color.Yellow
        4 -> Color.Blue
        5 -> Color.Magenta
        6 -> Color.Cyan
        else -> Color.White
    }
}

operator fun Color.plus(color: Color):Color {
    return Color(blue + color.blue, green + color.green, red + color.red, alpha + color.alpha)
}

private fun Int.zeroOrMinusOne() = if(this == 0){ 0 }else{this - 1}

private fun <T> MutableList<T>.padEnd(newEnd:Int, v:T) =
    this.addAll(List(max(newEnd - this.size, 0)) { v }).let { this }