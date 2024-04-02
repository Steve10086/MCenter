package com.astune.data.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.astune.model.ShellContent

class ANSICommendDecoder (val content: ShellContent){
    fun decodeCommend(text: String){
        decode(content, text)
    }
}

val regex = "^(\\d+;)*[?\\d+]?(\\d+)?([a-zA-Z])".toRegex()

/**
 * decode ansi terminal codes from the linux server
 *
 * @param content container of result
 * @param text from the shell
 * */
fun decode(content: ShellContent, text:String){
    var style = SpanStyle()
    val size = text.lines().size - 1
    val lines = text.replace("([^\b])||".toRegex(), "").lines()

    for (p in 0 .. size){
        var line = lines[p]

        val block = StringBuilder()

        while(line != ""){
            while(line.isNotEmpty() && line[0] == ''){
                content.deleteAtPointer()
                line = line.drop(1)
            }
            line = line.dropWhile {c ->
                if(c != '') {
                    block.append(c)
                    true
                }else{
                    false
                }
            }

            block.toString().removeSuffix("\u001B").takeIf { it != "" }?.let {
                content.insert(rendText(it, style))
                block.delete(0, block.length)
            }


            if(line.startsWith("\u001B[")){
                line = line.drop(2)
                regex.find(line)?.let {
                    val params = ShellFunctionParam(it.value.dropLast(1).split(";").map{
                        return@map if (it == "" || it.contains("?")) 0 else it.toInt()
                    }) //reformat
                    line = line.removePrefix(it.value)

                    params.params += MutableList(5){null}


                    //execute function
                    when(it.value.last()){
                        //color
                        'm' -> {
                            ANSIToStyle(params).also { result ->
                                style = if (result != SpanStyle()){
                                    style.merge(result)
                                }else{
                                    SpanStyle()
                                }
                            }
                        }
                        //movement
                        'A' -> content.movePointer(0, -(params[0]?:1))
                        'B' -> content.movePointer(0, params[0]?:0)
                        'C' -> content.movePointer(params[0]?:1, 0)
                        'D' -> content.movePointer(-(params[0]?:1), 0)
                        'E' -> {
                            content.movePointer(0, params[0]?:1)
                            execMovePointerAbs(content, 0)
                        }
                        'F' -> {
                            content.movePointer(0, -(params[0]?:1))
                            execMovePointerAbs(content, 0)
                        }
                        'f','G' -> execMovePointerAbs(content, params[0]?:1)
                        'H' -> execMovePointerAbs(content, params[1]?:0, params[0]?:0)

                        //deletion
                        'J' -> execDeleteLine(content, params[0]?:0)
                        'K' -> execDelete(content, params[0]?:0)
                        //scroll
                        'r' -> content.moveBoundsAbs(params[0], params[1])
                        'S' -> execScroll(content, -(params[0]?:1))
                        'T' -> execScroll(content, params[0]?:1)
                        //pointer
                        's' -> content.storePointer()
                        'u' -> content.restorePointer()

                        else -> {}
                    }
                }
            }else{
                line = line.drop(1)
            }
        }
        if(p < size){
            content.insert(AnnotatedString("\n"))
        }
    }


    (content.content[0].text == "\n" && content.content.size != 1).let {
        content.content.drop(1)
    }
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
        1 -> return singleParamStyles(params[0]?:0, style)
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


fun tripleParamStyles(
    values:ShellFunctionParam,
    style: SpanStyle
): SpanStyle {

    assert(values.size == 3)

    return when(values[0]){
        38 -> style.copy(color = Color(values[2]?:0))
        48 -> style.copy(background = Color(values[2]?:0))
        else -> {style}
    }
}

fun rgbStyles(
    values:ShellFunctionParam,
    style: SpanStyle
): SpanStyle{

    assert(values.size == 5)

    return when(values[0]){
        38 -> style.copy(color = Color(values[2]?:0,values[3]?:0,values[4]?:0))
        48 -> style.copy(background = Color(values[2]?:0,values[3]?:0,values[4]?:0))
        else -> {style}
    }
}

//default colors in shell
fun colorPlate(color:Int): Color {
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

/**
 * function param structure
 * */
data class ShellFunctionParam(
    var params:MutableList<Int?>,
    var size:Int = params.size

){
    fun changeSize(size:Int){
        this.size = size
    }

    operator fun get(i: Int): Int? {
        return params[i]
    }
}
fun ShellFunctionParam(params: List<Int?>): ShellFunctionParam {
    return ShellFunctionParam(params = params.toMutableList())
}