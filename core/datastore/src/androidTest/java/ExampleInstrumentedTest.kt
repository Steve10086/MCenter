package com.astune.datastore

import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import kotlin.test.assertEquals


class ExampleInstrumentedTest {
    val userPreferencesSerializer = UserPreferencesSerializer()
    @Test
    fun writeSetting() = runTest{
        val expectV = userPreferences {
            email = "test1@gmail.com"
        }

        val outputStream = ByteArrayOutputStream()

        userPreferencesSerializer.writeTo(expectV, outputStream)

        val inputStream = ByteArrayInputStream(outputStream.toByteArray())

        val actualV = userPreferencesSerializer.readFrom(inputStream)

        assertEquals(
            actualV,
            expectV,
        )
    }
}