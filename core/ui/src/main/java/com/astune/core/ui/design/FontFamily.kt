package com.astune.core.ui.design

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.astune.core.ui.R

val chakraPatchFamily = FontFamily(
    Font(R.font.chakrapetch_light, FontWeight.Light),
    Font(R.font.chakrapetch_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.chakrapetch_regular, FontWeight.Normal),
    Font(R.font.chakrapetch_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.chakrapetch_medium, FontWeight.Medium),
    Font(R.font.chakrapetch_bold, FontWeight.Bold),
    Font(R.font.chakrapetch_bolditalic, FontWeight.Bold, FontStyle.Italic),
)

val antaFamily = FontFamily(
    Font(R.font.anta_regular, FontWeight.Normal)
)