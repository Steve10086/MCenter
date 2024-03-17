package com.astune.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import kotlin.math.max

class ShellContent(
    content: SnapshotStateList<AnnotatedString> = mutableStateListOf(AnnotatedString("")),
    windowsHeight: Int = 0
) {
    var windowsHeight = windowsHeight

    var bounds = Pair(0, windowsHeight)
        private set
    var content = content
        private set
    var pointer = defaultPointer()
        private set

    var header:String? = null

    fun currentLineLength() = content[pointer.second + bounds.first].length - 1

    fun currentContentSize() = content.size - 1

    fun lastLineLength() = content.last().length - 1

    fun defaultPointer() = Pair(lastLineLength(), currentContentSize())

    fun defaultBounds() = with(max(currentContentSize() - windowsHeight, 0)){
        Pair(this , this + windowsHeight)
    }


    private var oldPointer = pointer

    fun movePointerAbs(x: Int? = pointer.first, y: Int? = pointer.second) {
        pointer = Pair(x?: pointer.first, y?:pointer.second)

        content.padEnd(pointer)
    }

    fun movePointer(x: Int, y: Int) {
        val newX = with(pointer.first + x) {
            if (this < 0) {
                return@with 0
            }
            return@with this
        }

        val newY = with(pointer.second + y) {
            if (this < 0) {
                return@with 0
            }
            return@with this
        }

        pointer = Pair(newX, newY)

        content.padEnd(pointer)
    }

    fun moveBoundsAbs(up: Int, down: Int) {
        bounds = Pair(up, down)
    }

    fun moveBounds(up: Int, down: Int) {
        bounds = Pair(bounds.first + up, bounds.second + down)
    }

    /**
     * delete content within the giving range
     * */
    fun delete(range: IntRange) {
        content.removeAll(content.filterIndexed { index, _ ->
            index in range
        })
        movePointer(0, pointer.second - range.count())
        content += AnnotatedString("")
    }

    /**
     * delete the text at position within range
     * */
    fun deleteLine(y: Int, range: IntRange) {
        val index = y + bounds.first
        val oldLength = content[index].length
        buildAnnotatedString {
            range.first.takeIf {
                it != 0
            }?.let {
                append(
                    content[index].subSequence(TextRange(0, it))
                )
            }
            range.last.takeIf {
                it != content[index].length - 1
            }?.let {
                append(
                    content[index].subSequence(TextRange(it, content[index].length - 1))
                )
            }
        }.takeIf {
            it.text != ""
        }?.let {
            content[index] = it
            if (pointer.second == index) {
                movePointer(-(oldLength - 1 - currentLineLength()), 0) // move the pointer to follow the edited text
            }
            return
        }
        delete(index..index)
    }

    /**
     * single char deletion
     * */
    fun deleteAtPointer() {
        if (pointer == defaultPointer()) {
            if(content.last().isEmpty()){
                content.dropLast(1)
            }else{
                content[content.size - 1] = content.last().subSequence(0, currentLineLength())
                content.removeIf {
                    it.isEmpty()
                }
            }
            pointer = defaultPointer()
            return
        }
        content[pointer.first] = content[pointer.second].subSequence(TextRange(0, pointer.first)) +
                content[pointer.second].subSequence(TextRange(pointer.first, currentLineLength()))
    }

    fun storePointer() {
        oldPointer = Pair(pointer.first, pointer.second)
    }

    fun restorePointer() {
        pointer = Pair(oldPointer.first, oldPointer.second)
    }

    fun insert(lines: AnnotatedString){
        if(pointer == defaultPointer()){
            this += lines
        }else{
            val currentLine = content[pointer.second]
            var end = AnnotatedString("")
            if(currentLine.isNotEmpty()){
                end = currentLine.subSequence(TextRange(pointer.first, currentLineLength()))
            }
            lines.lines().also {it ->
                it.forEachIndexed { index, line ->
                    val temporaryLine :AnnotatedString = buildAnnotatedString {
                        line.dropWhile {
                            it != line.last()
                        }
                    }

                    content[pointer.second] = temporaryLine + with(AnnotatedString.Builder(content[pointer.second])){
                        pop(temporaryLine.length)
                        toAnnotatedString()
                    }
                    if(index != it.lastIndex){
                        movePointer(0,1)
                    }
                }

                with(currentLine.subSequence(TextRange(0, pointer.first)).plus(AnnotatedString(it[0], lines.spanStyles))) {
                    content[pointer.second] = this
                }

                content.addAll(
                    pointer.second + 1,
                    it.drop(1).map{
                        AnnotatedString(it, lines.spanStyles)
                    }
                )
                movePointer(0, it.size - 2)
                movePointerAbs(x = content[pointer.second].length - 1)

                content[pointer.second] += end
            }
        }
    }

    /**
     * automatically moving bound when new line are added
     * */

    operator fun plusAssign(lines: List<AnnotatedString>) {
        this.content += lines
        pointer = defaultPointer()
    }

    operator fun plusAssign(lines: AnnotatedString) {
        lines.lines().also {
            with(content.last().plus(AnnotatedString(it[0], lines.spanStyles))) {
                content.removeAt(content.size - 1)
                content += this
            }
            for (line in it.drop(1)) {
                this.content += listOf(AnnotatedString(line, lines.spanStyles))
            }
            pointer = defaultPointer()
        }
    }

}


private fun MutableList<AnnotatedString>.padEnd(newEnd: Pair<Int, Int>) {
    val newX = newEnd.first
    val newY = newEnd.second
    if (this.size <= newY) {
        this += List(newY - this.size + 1) {
            AnnotatedString("\n")
        }
    }
    if (this[newY].length <= newX) {
        this[newY] += AnnotatedString("\t".repeat(newX - this[newY].length + 1))
    }
}