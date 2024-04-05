package com.astune.core.common.test

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class MCTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, MCBase_Application::class.java.name, context)
    }
}