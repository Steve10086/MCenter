package com.astune.mcenter.navigation

enum class TopLevelRoute(
    val routeName:String
) {
    DEVICE("device"),
    SETTING("setting"),
    LINK("linkPanel/{id}");

    companion object{
        fun nameList(): MutableList<String> {
            val names = mutableListOf<String>()
            for(value in TopLevelRoute.values()){
                names += value.routeName
            }
            return names
        }
    }

}

