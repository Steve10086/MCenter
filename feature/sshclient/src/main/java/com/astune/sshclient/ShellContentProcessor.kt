package com.astune.sshclient

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle


fun decode(content:ShellContent, text:String){
    var newLines = AnnotatedString("")
    val regex = "^(\\d+;)*(\\d+)?([a-zA-Z])".toRegex()

    var style = SpanStyle()

    content += text.lines()
    for (p in content.bounds.second until content.content.size){
        var line = content.content[p]
        val block = StringBuilder()
        newLines.plus(AnnotatedString("\n"))
        while(line != ""){
            line = line.dropWhile {
                block.append(it)
                it != '\\'
            }

            newLines.plus(rendText(block.toString().removeSuffix("\\"), style))
            block.delete(0, block.length)

            if(line.substring(0..3) == "033["){
                line.drop(4)
                regex.find(line)?.let {
                    val params = it.value.removeSuffix("m").split(";").map{ it.toInt() } // format

                    if(params.isEmpty()){
                        params + 0 // default param
                    }
                    when(it.value.last()){
                        'm' -> style = ANSIToStyle(params)
                        'J' -> execDelete(content, params[0])
                    }
                }
            }
        }
    }
}


fun execDelete(content: ShellContent, commend:Int){
    when(commend){
        0 -> content.delete(content.bounds.first, content.pointer.second)
        1 -> content.delete(content.pointer.second, content.bounds.second)
        2 -> content.delete(content.bounds.first, content.bounds.second)
        3 -> content.delete(0, content.content.size - 1)
    }
}

fun movePointer(content: ShellContent, commend:Int){

}
fun execScroll(){}

fun rendText(text:String, style: SpanStyle): AnnotatedString{
    val result = buildAnnotatedString {
        while(true){
            withStyle(
                style = style
            ){
                append(text)
            }
        }
    }

    return result
}


fun ANSIToStyle(
    params:List<Int>,
    style: SpanStyle = SpanStyle()
):SpanStyle{
    when (params.size){
        1 -> return singleParamStyles(params[0], style)
        3 -> return tripleParamStyles(params, style)
        5 -> return RGBStyles(params, style)
    }
    return style
}

fun singleParamStyles(
    value:Int,
    style: SpanStyle
):SpanStyle{
    when (value){
        0 -> return SpanStyle()
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
    }
    return style
}


fun colorPlate(color:Int):Color{
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

fun tripleParamStyles(
    values:List<Int>,
    style: SpanStyle
): SpanStyle {

    assert(values.size == 3)

    when(values[0]){
        38 -> style.copy(color = Color(values[2]))
        48 -> style.copy(background = Color(values[2]))
    }
    return style
}

fun RGBStyles(
    values:List<Int>,
    style: SpanStyle
): SpanStyle{

    assert(values.size == 5)

    when(values[0]){
        38 -> style.copy(color = Color(values[2],values[3],values[4]))
        48 -> style.copy(background = Color(values[2],values[3],values[4]))
    }
    return style
}

data class ShellContent(
    val content: MutableList<String> = mutableListOf(),
    var pointer: Pair<Int, Int> = Pair(0,0),
    var bounds: Pair<Int, Int> = Pair(0,0)
){
    fun movePointerAbs(x:Int, y:Int){
        pointer = Pair(x,y)
        if(content[y].length < x){
            content[y].padEnd(x)
        }
    }

    fun movePointer(x:Int, y:Int){
        pointer = pointer.apply {
            this.first + x
            this.second + y
        }
        if(content[y].length < pointer.first){
            content[y].padEnd(pointer.first)
        }
    }

    fun moveBounds(up:Int, down:Int){
        bounds = bounds.apply {
            this.first + up
            this.second + down
        }
    }

    fun delete(from:Int, to:Int){
        content.removeAll(content.filterIndexed { index, _ ->
            index in from..to })
    }

    operator fun plusAssign(lines: List<String>) {
        this.content += lines
    }
}


private operator fun Color.plus(color: Color):Color {
    return Color(blue + color.blue, green + color.green, red + color.red, alpha + color.alpha)
}