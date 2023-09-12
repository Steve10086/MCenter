package com.astune.datastore

import com.astune.model.UserInfo
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import kotlin.test.assertEquals

@HiltAndroidTest
class setUserDataTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var userDataSource: UserDataSource

    @Before
    fun init(){
        hiltRule.inject()
    }

    @Test
    fun writeEmail() = runTest{
        userDataSource.setUserData(UserInfo(email = "test2@gmail.com"))
        assertEquals(
            userDataSource.userData.first().email,
            "test2@gmail.com"
        )
    }
}