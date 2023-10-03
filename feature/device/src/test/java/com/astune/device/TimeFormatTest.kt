package com.astune.device

import org.junit.Test
import java.time.Duration
import java.time.Instant
import kotlin.test.assertEquals

class TimeFormatTest {
    @Test
    fun timeFormatTest(){
        val time = Instant.now().toString()
        val timeBetween = Instant.ofEpochSecond(Duration.between(Instant.parse("2022-09-17T00:00:00Z"), Instant.now()).seconds).toString()
        val min = timeBetween.subSequence(14, 16).toString().toInt()
        val hour = timeBetween.subSequence(11, 13).toString().toInt()
        val day = timeBetween.subSequence(8, 10).toString().toInt() - 1
        val month = timeBetween.subSequence(5, 7).toString().toInt() - 1
        val year = timeBetween.subSequence(0, 4).toString().toInt() - 1970
        assertEquals("$min", "34")
        assertEquals("$hour", "9")
        assertEquals("$day", "0")
        assertEquals("$month", "0")
        assertEquals("$year", "1")
    }
}