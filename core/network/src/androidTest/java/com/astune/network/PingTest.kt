package com.astune.network
import android.util.Log
import com.astune.core.network.utils.averagePing
import com.astune.core.network.utils.ping
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals


class PingTest {
    @Test
    fun testPing() = runTest{
        val result = averagePing("192.168.1.114",  timeout = 10)
        assertEquals(result.toInt(),4087)
    }

    @Test
    fun testRegex() = runTest {
        val result = ping("127.0.0.1",  timeout = 10, time = 5)
        Log.d("test", result[2])
        Regex("""\d+\.\d+(?= ?ms)""").find(result[2])?.let { Log.d("test", it.value) }
        assertEquals((Regex("""\d+\.\d+(?= ?ms)""").find(result[2])?.value?.toDouble() ?:-1).toInt(),0)
    }
}