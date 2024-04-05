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
        //sshRepository.executeShellCommand("192.168.1.116", "user", "1", "ping 192.168.1.1")
        val connection = sshRepository.connect("192.168.1.116", 22, "user", "1")
        Log.d("Test", "Connected!")
        assert(connection != null)
        "ping 192.168.1.1\n".toByteArray().forEach {
            sshRepository.send(it, connection!!.shell)
        }

        sshRepository.receive(connection!!.shell).collect(){
            Log.d("Test", it)
            //sshRepository.disconnect(connection)
        }

    }
}