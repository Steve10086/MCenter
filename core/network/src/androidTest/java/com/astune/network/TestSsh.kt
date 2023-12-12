package com.astune.network

import android.util.Log
import com.astune.core.network.repository.SSHRepository
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class TestSsh {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var sshRepository: SSHRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun testConnection() = runTest {
        assertEquals(2, 2)
        val connection = sshRepository.connect("192.168.1.1", 2223, "root", "1234")
        sshRepository.receive(connection.shell).collect(){
            Log.d("Test", it.toString())
        }
        sshRepository.send("ping -c 1 google.com\n".toByteArray(), connection.shell)
        sshRepository.receive(connection.shell).collect(){
            Log.d("Test", it.toString())
        }
    }
}