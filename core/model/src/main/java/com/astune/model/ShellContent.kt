package com.astune.model

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
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

    var headerList = MutableList(content.size){
        false
    }
        private set

    fun currentLineLength() = content[pointer.second + bounds.first].length - 1

    fun currentContentSize() = content.size - 1

    fun lastLineIndex() = content.last().lastIndex

    fun defaultPointer() = Pair(lastLineIndex(), currentContentSize())

    fun defaultBounds() = with(max(currentContentSize() - windowsHeight, 0)){
        Pair(this , this + windowsHeight)
    }


    private var oldPointer = pointer

    fun movePointerAbs(x: Int? = pointer.first, y: Int? = pointer.second) {
        pointer = Pair(x?: pointer.first, y?:pointer.second)
        //update header if new lines are generated
        headerList += List(max((y?:pointer.second) - currentContentSize(), 0)){false}

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
        //update header if new lines are generated
        headerList += List(max(newY - currentContentSize(), 0)){false}

        pointer = Pair(newX, newY)

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
                it > 0
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
                movePointer(-(oldLength - 1 - currentLineLength()), 0) // move the pointer to follow the edited text
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
                headerList.dropLast(1)
            }else{
                content[content.size - 1] = content.last().subSequence(0, currentLineLength())
                content.removeIf {
                    it.isEmpty()
                }
            }
            pointer = defaultPointer()
            return
        }
        if(content[pointer.second].isEmpty() || content[pointer.second].text == "\t"){
            content.removeAt(pointer.second)
            headerList.removeAt(pointer.second)
        }else{
            content[pointer.second] = content[pointer.second].subSequence(TextRange(0, pointer.first)) +
                    content[pointer.second].subSequence(TextRange(pointer.first + 1, content[pointer.second].length))
        }
    }

    fun storePointer() {
        oldPointer = Pair(pointer.first, pointer.second)
    }

    fun restorePointer() {
        pointer = Pair(oldPointer.first, oldPointer.second)
    }

    fun setHeaderAt(index:Int, value:Boolean){headerList.getOrNull(index)?.let{
        headerList[index] = value
    }}

    fun insert(lines: AnnotatedString, isHeader:Boolean? = null){
        if(pointer == defaultPointer()){
            this += lines.cleanStyles()
        }else{
            //if pointer is not at end, replace anything with new input
            lines.lines().also {it ->
                it.forEachIndexed { index, l ->
                    var line = l
                    if(header != null && pointer.first == 0){
                        if(line.startsWith(header!!)){
                            line = line.replace(header!!, "")
                            headerList[pointer.second] = true
                        }else{
                            headerList[pointer.second] = false
                        }
                    }

                    val temporaryLine = AnnotatedString(line, lines.spanStyles)

                    //replace the old line with newline at the start
                    content[pointer.second] = (content[pointer.second].subSequence(0, pointer.first) +
                        if(temporaryLine.lastIndex + pointer.first >= currentLineLength()){
                            temporaryLine
                        }else{
                            temporaryLine + content[pointer.second].subSequence(TextRange(temporaryLine.length + pointer.first, content[pointer.second].length))
                        }).cleanStyles()


                    isHeader?.let{headerList[pointer.second] = it}

                    if(index != it.lastIndex){
                        movePointerAbs(0)
                        movePointer(0,1)
                    }else{
                        movePointer(temporaryLine.length, 0)
                    }
                }
            }
        }
    }

    fun toAnnotatedString() = buildAnnotatedString {
        var headerSpace = 0
        content.onEachIndexed { index, line ->
            if(headerList.getOrElse(index){false}){
                withStyle(HeaderStyle){
                    append(header)
                }
                headerSpace += header?.length ?: 0
            }
            for(style in line.spanStyles.filter {style ->
                style.item != SpanStyle()
            }){
                val offset = this.length + headerSpace
                addStyle(
                    style = style.item, start = style.start + offset, end = style.end + offset
                )
            }
            append(line.text + "\n")

        }
    }


    private operator fun plusAssign(lines: List<AnnotatedString>) {
        lines.onEach { line ->
            if(header!=null){
                header?.let{prefix ->
                    headerList += if(line.contains(prefix)){
                        content += line.subSequence(startIndex = prefix.length, line.length)
                        true
                    }else{
                        content += line
                        false
                    }
                }
            }else{
                content += line
                headerList += false
            }
        }
        pointer = defaultPointer()
    }

    private operator fun plusAssign(lines: AnnotatedString) {
        lines.lines().also {
            if(header!=null){
                header?.let{prefix ->
                    headerList[currentContentSize()] = it[0].contains(prefix) || headerList[currentContentSize()]
                }
            }
            content[currentContentSize()] += AnnotatedString(it[0].removePrefix(header?:""), lines.spanStyles)

            for (line in it.drop(1)) {
                this += listOf(AnnotatedString(line, lines.spanStyles))
            }
            if(it.drop(1).isEmpty()){
                pointer = defaultPointer()
            }
        }
    }
}

val HeaderStyle = SpanStyle(fontWeight = FontWeight.Medium)

/**
 * padding vertically with empty annotatedString,
 * then padding horizontally with space
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
        this[newY] += AnnotatedString("\u3000".repeat(max(newX - this[newY].length, 0)))//horizontally padding
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