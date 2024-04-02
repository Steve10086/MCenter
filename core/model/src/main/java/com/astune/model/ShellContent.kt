package com.astune.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import kotlin.math.max

class ShellContent(
    content: SnapshotStateList<AnnotatedString> = mutableStateListOf(AnnotatedString("")),
    var windowsHeight: Int = 0
) {

    var bounds = Pair(0, windowsHeight)
        private set

    var content = content
        private set

    var pointer = defaultPointer()
        private set

    fun currentLineLength() = content[pointer.second].length - 1

    fun currentContentSize() = content.size - 1

    fun lastLineIndex() = content.last().lastIndex

    fun defaultPointer() = Pair(lastLineIndex(), currentContentSize())

    fun defaultBounds() = with(max(currentContentSize() - windowsHeight, 0)){
        Pair(this , this + windowsHeight)
    }

    private var oldPointer = pointer

    fun movePointerAbs(x: Int? = pointer.first, y: Int? = pointer.second) {
        pointer = Pair(x?: pointer.first, y?:pointer.second)

        content.padEnd(pointer)
        bounds = defaultBounds()
    }

    fun movePointer(x: Int, y: Int) {
        val newX = with(pointer.first + x) {
            if (this < -1) {
                return@with -1
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
        bounds = defaultBounds()
        content.padEnd(pointer)

    }

    fun moveBoundsAbs(up: Int?, down: Int?) {
        bounds = Pair(up?:bounds.first, down?:bounds.second)
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
        movePointer(0, - (pointer.second - range.first).positiveOrZero())
        if(content.isEmpty()) content += AnnotatedString("")
    }

    /**
     * delete the text at position within range
     * */
    fun deleteLine(y: Int, range: IntRange) {
        val index = y
        val oldLength = content[index].length
        buildAnnotatedString {
            range.first.takeIf {
                it >= 0
            }?.let {
                append(
                    content[index].subSequence(TextRange(0, it))
                )
            }
            range.last.takeIf {
                it < content[index].length
            }?.let {
                append(
                    content[index].subSequence(TextRange(it, content[index].length))
                )
            }
        }.takeIf {
            it.text != ""
        }?.let {
            content[index] = it
            if (pointer.second == index) {
                movePointer(-(oldLength - currentLineLength()), 0) // move the pointer to follow the edited text
            }
            return
        }
    }

    /**
     * single char deletion
     * */
    fun deleteAtPointer() {
        if (pointer == defaultPointer()) {
            if(content.last().isEmpty()){
                content.dropLast(1)
            }else{
                content[currentContentSize()] = content.last().subSequence(0, currentLineLength())
            }
            pointer = defaultPointer()
        }else{
            if(content[pointer.second].isEmpty()){
                content.removeAt(pointer.second)
                movePointer(0, -1)
                movePointerAbs(x = currentLineLength())
            }else{
                content[pointer.second] =
                    // if pointer at zero, drop the first char, else taking chars before the pointer
                    content[pointer.second].subSequence(
                        TextRange(0, pointer.first.positiveOrZero())
                    ) +
                    // if the pointer at end, drop the last char, else taking chars after the pointer
                    content[pointer.second].subSequence(
                        TextRange(
                            pointer.first + 1,
                            content[pointer.second].length
                        )
                    )

                movePointer(-1, 0)
            }
        }
    }

    fun storePointer() {
        oldPointer = Pair(pointer.first, pointer.second)
    }

    fun restorePointer() {
        pointer = Pair(oldPointer.first, oldPointer.second)
    }

    /**
     * insert the given line after the pointer
     * */
    fun insert(lines: AnnotatedString){
        if(pointer == defaultPointer()){
            this += lines.cleanStyles()
        }else{
            //if pointer is not at end, replace anything with new input
            lines.lines().also {
                it.forEachIndexed { index, line ->
                    val temporaryLine = AnnotatedString(line, lines.spanStyles)

                    if(content[pointer.second].isEmpty()){
                        content[pointer.second] = temporaryLine.cleanStyles()
                    }else{
                        //replace the old line with newline at the start
                        content[pointer.second] = (content[pointer.second].subSequence(0, pointer.first + 1) +
                                // if new line will exceed the original length, replace the rest
                                if(temporaryLine.lastIndex + pointer.first + 1 >= currentLineLength()){
                                    temporaryLine
                                }else{
                                    temporaryLine + content[pointer.second].subSequence(
                                        TextRange(
                                            temporaryLine.length + pointer.first + 1,
                                            content[pointer.second].length
                                        )
                                    )

                                }).cleanStyles()
                    }

                    if(index != it.lastIndex){
                        movePointerAbs(-1)
                        movePointer(0,1)
                    }else{
                        movePointer(temporaryLine.length, 0)
                    }
                }
            }
        }
    }

    fun toAnnotatedString() = buildAnnotatedString {
        content.onEachIndexed { index, line ->
            for(style in line.spanStyles.filter {style ->
                style.item != SpanStyle()
            }){
                val offset = this.length
                addStyle(
                    style = style.item, start = style.start + offset, end = style.end + offset
                )
            }
            if (index == pointer.second){
                addStyle(
                    style = PointerStyle, start = pointer.first + this.length, end = pointer.first + 1 + this.length
                )
            }
            append(line.text + "\n")
        }
    }


    private operator fun plusAssign(lines: List<AnnotatedString>) {
        lines.onEach { line ->
            content += line
        }
        pointer = defaultPointer()
        bounds = defaultBounds()
    }

    private operator fun plusAssign(lines: AnnotatedString) {
        lines.lines().also {

            content[currentContentSize()] += AnnotatedString(it[0], lines.spanStyles)

            for (line in it.drop(1)) {
                this += listOf(AnnotatedString(line, lines.spanStyles))
            }
            if(it.drop(1).isEmpty()){
                pointer = defaultPointer()
            }
        }
    }
}

private fun Int.positiveOrZero() =
    if(this < 0){
        0
    }else {
        this
    }


/**
 * padding vertically with empty annotatedString,
 * then padding horizontally with space
 * return true if padding applied
 * */
private fun MutableList<AnnotatedString>.padEnd(newEnd: Pair<Int, Int>) {
    val newX = newEnd.first
    val newY = newEnd.second
    if (this.size <= newY) {
        this += List(newY - this.size + 1) {//vertically padding
            AnnotatedString("")
        }
    }
    if (this[newY].length <= newX) {
        this[newY] += AnnotatedString("\u3000".repeat(max(newX - this[newY].lastIndex, 0)))//horizontally padding
    }
}

/**
 * return a new AnnotatedString with all empty or zero length styles removed
 * */
fun AnnotatedString.cleanStyles() = buildAnnotatedString {
    for(style in this@cleanStyles.spanStyles){
        if(style.end != style.start && style.item != EmptyStyle){
            addStyle(style.item, style.start, style.end)
        }
    }
    append(this@cleanStyles.text)
}

val EmptyStyle = SpanStyle()

val PointerStyle = SpanStyle(background = Color.LightGray)