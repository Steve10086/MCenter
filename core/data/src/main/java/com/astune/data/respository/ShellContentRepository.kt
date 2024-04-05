package com.astune.data.respository

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import com.astune.data.utils.*
import com.astune.model.ssh.*
import com.astune.model.ssh.Action.*
import com.astune.model.ssh.ShellFunctionParam.Companion.INVALID_PARAM
import com.astune.model.ssh.ShellFunctionParam.Companion.NEGATIVE_INF
import javax.inject.Inject
import kotlin.math.min


val regex = "^(\\d+;)*[?\\d+]?(\\d+)?(.)".toRegex()
class ShellContentRepository @Inject constructor(
    val shellState: ShellState
) {
    /**
     * Ctrl convertor from termux:
     * https://github.com/termux/termux-app/blob/master/terminal-view/src/main/java/com/termux/view/TerminalView.java
     * */
    private fun processCtrl(char:Char): Int {
        var codePoint = char.code
        if (codePoint >= 'a'.code && codePoint <= 'z'.code) {
            codePoint = codePoint - 'a'.code + 1
        } else if (codePoint >= 'A'.code && codePoint <= 'Z'.code) {
            codePoint = codePoint - 'A'.code + 1
        } else if (codePoint == ' '.code || codePoint == '2'.code) {
            codePoint = 0
        } else if (codePoint == '['.code || codePoint == '3'.code) {
            codePoint = 27 // ^[ (Esc)
        } else if (codePoint == '\\'.code || codePoint == '4'.code) {
            codePoint = 28
        } else if (codePoint == ']'.code || codePoint == '5'.code) {
            codePoint = 29
        } else if (codePoint == '^'.code || codePoint == '6'.code) {
            codePoint = 30 // control-^
        } else if (codePoint == '_'.code || codePoint == '7'.code || codePoint == '/'.code) {
            // "Ctrl-/ sends 0x1f which is equivalent of Ctrl-_ since the days of VT102"
            // - http://apple.stackexchange.com/questions/24261/how-do-i-send-c-that-is-control-slash-to-the-terminal
            codePoint = 31
        } else if (codePoint == '8'.code) {
            codePoint = 127 // DEL
        }
        return codePoint
    }

    fun processKey(char:Char):ByteArray {
        var result = byteArrayOf(char.code.toByte())
        with(shellState.keyStateMap){
            if(this.getOrDefault(CustomKey.Ctrl, false)){
                result[0] = processCtrl(char).toByte()
            }
            if(char == '\n'){
                result[0] = '\r'.code.toByte()
            }
            if(this.getOrDefault(CustomKey.Alt, false)){
                result += char.code.toByte()
                // (ESC) + char
                result[0] = 27
            }
        }
        return result
    }

    fun processExtraKey(key:CustomKey):ByteArray?{
        with(shellState.shellMode){
            val header = if (this.getOrDefault(InputState.DECCKM, false)) "\u001bO" else "\u001B["

            return when(key){
                CustomKey.Up -> header.plus("A").toByteArray()
                CustomKey.Down -> header.plus("B").toByteArray()
                CustomKey.Right -> header.plus("C").toByteArray()
                CustomKey.Left -> header.plus("D").toByteArray()
                CustomKey.Tab -> "\t".toByteArray()
                CustomKey.Esc -> "\u001B".toByteArray()
                else->{
                    shellState.switchKey(key)
                    null
                }
            }
        }

    }

    /**
     * decode ansi terminal codes from the linux server
     *
     * @param content container of result
     * @param input from the shell
     * */
    fun decode(content: ShellContent, input: String) {
        var style = SpanStyle()
        var newText = getPureText(input)
        var text = input.drop(newText.length)
        while(
            newText.isNotEmpty() ||
            text.isNotEmpty()
        ){
            newText = cleanText(content, newText)
            if(newText.isNotEmpty()){
                content.insert(
                    AnnotatedString(newText, style)
                )
            }
            text = text.removePrefix("\u001B")
            when(text.getOrNull(0)){
                '[','=','>' ->{
                    text = text.removePrefix("[")
                    regex.find(text)?.let { result ->
                        text = text.removePrefix(result.value)
                        val action = decodeEscapeSeries(result.value)
                        action?.let {action1 ->
                            action1.takeIf { it == CHANGE_STYLE }?.let{
                                style = solveStyle(it.param, style)
                                null
                            }?: execAction(content, action1)

                            if(action == DELETE && action.param[0] == 0){
                                if(text.contains("\n")){
                                    text.replaceBefore("\n", "")
                                }else{
                                    text = ""
                                }
                            }
                        }
                    }
                }
            }


            newText = getPureText(text)
            text = text.drop(newText.length)
        }
    }

    private fun cleanText(content: ShellContent, input: String):String{
        var result = input.replace("([^\b])|||".toRegex(), "")
        while(result.startsWith('\b')){
            content.deleteAtPointer()
            result = result.drop(1)
        }
        return result
    }

    private fun execAction(content: ShellContent, action: Action){
        when(action){
            MOVE_CURSOR -> {
                /*moving at the direction if not negative inf(to the start)*/
                content.movePointer(
                    action.param[0].takeIf { it != NEGATIVE_INF }?: min(-content.pointer.first, 0),
                    action.param[1].takeIf { it != NEGATIVE_INF }?: -content.pointer.second
                )
            }
            MOVE_CURSOR_REVERSE -> {
                /*moving opposite if not negative inf(to the start)*/
                content.movePointer(
                    action.param[0].takeIf { it != NEGATIVE_INF }?.let { - it }?: min(-content.pointer.first, 0),
                    action.param[1].takeIf { it != NEGATIVE_INF }?.let { - it }?: -content.pointer.second
                )
            }
            MOVE_CURSOR_ABS -> {
                content.movePointerAbs(
                    action.param[0].takeIf { it != INVALID_PARAM },
                    action.param[1].takeIf { it != INVALID_PARAM }?.plus(content.bounds.first)
                )
            }
            DELETE -> {
                when(action.param[0]){
                    0 -> content.deleteLine(content.pointer.second, content.pointer.first + 1..content.currentLineLength() + 1)
                    1 -> content.deleteLine(content.pointer.second, 0..content.pointer.first)
                    2 -> content.delete(content.pointer.second..content.pointer.second)
                }
            }
            DELETE_LINE -> {
                when(action.param[0]){
                    0 -> {
                        content.delete(content.pointer.second + 1..content.bounds.second)
                        content.deleteLine(content.pointer.second, content.pointer.first + 1..content.currentLineLength() + 1)
                    }
                    1 -> content.delete(content.bounds.first..content.pointer.second)
                    2 -> content.delete(content.bounds.first..content.bounds.second)
                    3 -> content.delete(0 until content.content.size)
                }
            }
            STORE_CURSOR -> content.storePointer()
            RESTORE_CURSOR -> content.restorePointer()
            SCROLL -> content.moveBounds(action.param[0], action.param[0])
            SCROLL_REVERSE -> content.moveBounds(- action.param[0], - action.param[0])
            RESTRICT_BOUNDS -> content.moveBoundsAbs(action.param[0], action.param[1])
            SET_CURSOR_STATE -> shellState.cursorState = CursorState.APPLICATION
            DEC_MODE_ON -> {
                val state = InputState.values().firstOrNull { it.value == action.param[0] }
                state?.let { shellState.shellMode[it] = true }
            }
            DEC_MODE_OFF -> {
                val state = InputState.values().firstOrNull { it.value == action.param[0] }
                state?.let { shellState.shellMode[it] = false }
            }
            SET_ECMA_48_MODE -> {
                TODO()
            }

            SET_PASTE -> {
                TODO()
            }

            else -> {}
        }
    }

    private fun solveStyle(
        param:ShellFunctionParam,
        style: SpanStyle = SpanStyle()
    ):SpanStyle{
        val params = param.params
        when (params.size){
            0 -> return SpanStyle()
            1 -> return singleParamStyles(params[0], style)
            3 -> return tripleParamStyles(param, style)
            5 -> return rgbStyles(param, style)
        }
        return style
    }

    private fun getHeader(){}

}