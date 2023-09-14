package com.astune.core.network.utils


import java.io.BufferedReader
import java.io.InputStreamReader

fun ping(url: String): String {
    var str = ""
    try {
        val process = Runtime.getRuntime().exec("/system/bin/ping -c 1 $url")
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        var i: Int
        val buffer = CharArray(4096)
        val output = StringBuilder()
        while (reader.read(buffer).also { i = it } > 0) output.appendRange(buffer, 0, i)
        reader.close()
        str = output.toString()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return str
}