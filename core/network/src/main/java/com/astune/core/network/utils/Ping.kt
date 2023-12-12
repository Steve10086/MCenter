package com.astune.core.network.utils


import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

internal suspend fun averagePing(url: String, timeout: Int): Double {
    return ping(url, time = 4, timeout = timeout)
        .also {
            Log.i("Ping", it.toString())
        }.takeIf { it.size > 5 }?.subList(1, 3)?.map{ result ->
            (Regex("""\d+\.\d+(?= ?ms)""").find(result)?.value?:"-1").toDouble()
        }?.average() ?: -1.0

}

internal suspend fun ping(url: String, timeout: Int, time: Int): List<String> {
    val output = mutableListOf<String>()
    try {
        val process = Runtime.getRuntime().exec("ping -c $time -w $timeout $url")
        val reader = BufferedReader(InputStreamReader(process.inputStream))

        output.addAll(reader.readLines())
        reader.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return output
}