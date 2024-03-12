package com.astune.data.utils

import android.util.Size

operator fun Size.div(i: Size): Size {
    return Size(
        (this.width / i.width),
        (this.height / i.height)
    )
}