package com.astune.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange

class ShellContent(
    content: SnapshotStateList<AnnotatedString> = mutableStateListOf(AnnotatedString("")),
    pointer: Pair<Int, Int> = Pair(0,0),
    bounds: Pair<Int, Int> = Pair(0,0),
){
    var bounds = bounds
        private set
    var content = content
        private set
    var pointer = pointer
        private set

    val currentLineLength = content[pointer.first].length - 1
    private var oldPointer = pointer

    fun movePointerAbs(x:Int = pointer.first, y:Int = pointer.second){
        pointer = Pair(x, y)
        if(content.size <= y){
            content += List(y - content.size + 1){
                AnnotatedString(" ")
            }
        }
        if(content[y].length < x){
            content[y].padEnd(x)
        }
    }

    fun movePointer(x:Int, y:Int){
        pointer = Pair(pointer.first + x, pointer.second + y)
        if(content[y].length < pointer.first){
            content[y].padEnd(pointer.first)
        }
    }

    fun moveBoundsAbs(up:Int, down:Int){
        bounds = Pair(up, down)
    }

    fun moveBounds(up:Int, down:Int){
        bounds = Pair(bounds.first + up, bounds.second + down)
    }

    fun delete(range: IntRange){
        content.removeAll(content.filterIndexed { index, _ ->
            index in range })
        content += AnnotatedString("")
    }

    fun deleteLine(index:Int, range: IntRange){
        if (range.first == 0){
            content[index] = content[index].subSequence(TextRange(range.last + 1, currentLineLength))
        } else {
            content[index] = content[index].subSequence(TextRange(0, range.first))
        }
    }

    fun storePointer(){
        oldPointer = Pair(pointer.first, pointer.second)
    }

    fun restorePointer(){
        pointer = Pair(oldPointer.first, oldPointer.second)
    }

    operator fun plusAssign(lines: List<AnnotatedString>) {
        this.content += lines
    }

    operator fun plusAssign(lines: AnnotatedString){
        lines.lines().also {
            with(content.last().plus(AnnotatedString(it[0], lines.spanStyles))) {
                content.removeAt(content.size - 1)
                content += this
            }
            for(line in it.drop(1)){
                this.content += listOf(AnnotatedString(line, lines.spanStyles))
            }
        }
    }
}
data class ShellFunctionParam(
    var params:MutableList<Int>,
    var size:Int = params.size

){
    fun changeSize(size:Int){
        this.size = size
    }

    operator fun get(i: Int): Int {
        return params[i]
    }

}
fun ShellFunctionParam(params: List<Int>): ShellFunctionParam {
    return ShellFunctionParam(params = params.toMutableList())
}