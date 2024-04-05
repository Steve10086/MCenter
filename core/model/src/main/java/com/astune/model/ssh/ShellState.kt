package com.astune.model.ssh

import javax.inject.Inject

class ShellState @Inject constructor(){
    val shellMode:MutableMap<InputState, Boolean> = mutableMapOf()
    var cursorState: CursorState = CursorState.DEFAULT
    val keyStateMap:MutableMap<CustomKey, Boolean> = mutableMapOf()
    init {
        for(value in InputState.values()){
            shellMode[value] = value.initState
        }
        for(value in CustomKey.values()){
            keyStateMap[value] = false
        }
    }
    fun switchMode(state: InputState){
        shellMode[state] = !shellMode[state]!!
    }

    fun switchKey(key:CustomKey){
        keyStateMap[key] = !keyStateMap[key]!!
    }
}