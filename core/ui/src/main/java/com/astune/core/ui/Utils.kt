package com.astune.core.ui

import android.content.Context
import android.util.TypedValue
import androidx.compose.runtime.Composable

@Composable
fun spToPx(sp: Float, context: Context) = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.resources.displayMetrics)