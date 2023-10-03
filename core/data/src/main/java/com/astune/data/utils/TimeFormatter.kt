package com.astune.data.utils

import java.time.Duration
import java.time.Instant

/**
 * @return a formatted string in "yyyy years mm months dd days hh hours mm mins ago"
 * @param startTime
 * */
fun getTimeBetween(startTime:Instant):String{
    val timeBetween = Instant.ofEpochSecond(Duration.between(startTime, Instant.now()).seconds).toString()
    val min = timeBetween.subSequence(14, 16).toString().toInt()
    val hour = timeBetween.subSequence(11, 13).toString().toInt()
    val day = timeBetween.subSequence(8, 10).toString().toInt() - 1
    val month = timeBetween.subSequence(5, 7).toString().toInt() - 1
    val year = timeBetween.subSequence(0, 4).toString().toInt() - 1970
    val result:StringBuilder = java.lang.StringBuilder()
    year.takeIf { it > 0 }.let { result.append("$it years ") }
    month.takeIf { it > 0 }.let { result.append("$it months ") }
    day.takeIf { it > 0 }.let { result.append("$it days ") }
    hour.takeIf { it > 0 }.let {result.append("$it hours ") }
    min.takeIf { it > 0 || result.isBlank() }.let { result.append("$it mins ") }
    result.append("ago")
    return result.toString()
}