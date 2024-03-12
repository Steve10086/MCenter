package com.astune.core.ui.design.ssh

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

@Composable
fun MCSshTheme(
    color:SshColorScheme,
    content: @Composable () -> Unit
){
    val rememberedColorScheme = remember {
        color.copy()
    }.apply {
        updateColorSchemeFrom(color)
    }

    CompositionLocalProvider(
        localSshColorScheme provides rememberedColorScheme
    ){
        content()
    }
}

object MCSshTheme{
    val colorScheme:SshColorScheme
        @Composable
        @ReadOnlyComposable
        get() = localSshColorScheme.current
}


@Stable
class SshColorScheme(
    primary: Color,
    onPrimary: Color,
    secondary: Color,
    onSecondary: Color,
){
    var primary by mutableStateOf(primary, structuralEqualityPolicy())
        internal set
    var onPrimary by mutableStateOf(onPrimary, structuralEqualityPolicy())
        internal set
    var secondary by mutableStateOf(secondary, structuralEqualityPolicy())
        internal set
    var onSecondary by mutableStateOf(onSecondary, structuralEqualityPolicy())
        internal set

    fun copy():SshColorScheme{
        return SshColorScheme(
            primary, onPrimary, secondary, onSecondary
        )
    }
}

fun blackSshColorScheme(
    primary: Color = ssh_theme_black_primary,
    onPrimary: Color = ssh_theme_black_onPrimary,
    secondary: Color = ssh_theme_black_secondary,
    onSecondary: Color = ssh_theme_black_onSecondary,
):SshColorScheme{
    return SshColorScheme(
        primary,
        onPrimary,
        secondary,
        onSecondary
    )
}

fun violentSshColorScheme(
    primary: Color = ssh_theme_violent_primary,
    onPrimary: Color = ssh_theme_violent_onPrimary,
    secondary: Color = ssh_theme_violent_secondary,
    onSecondary: Color = ssh_theme_violent_onSecondary,
):SshColorScheme{
    return SshColorScheme(
        primary,
        onPrimary,
        secondary,
        onSecondary
    )
}

fun SshColorScheme.updateColorSchemeFrom(other:SshColorScheme){
    primary = other.primary
    onPrimary = other.onPrimary
    secondary = other.secondary
    onSecondary = other.onSecondary
}

internal val localSshColorScheme = staticCompositionLocalOf { violentSshColorScheme() }