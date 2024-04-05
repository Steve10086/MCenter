package com.astune.model

import android.graphics.Bitmap

class UserInfo(
    var name : String = "",
    var email : String = "",
    var enabledZerotier : Boolean = false,
    var zerotierPass : String = "",
    var theme : String = "",
    var sshTheme : String = "",
    var avatar : Bitmap = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
)