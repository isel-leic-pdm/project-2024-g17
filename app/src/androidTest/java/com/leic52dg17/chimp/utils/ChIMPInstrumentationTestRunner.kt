package com.leic52dg17.chimp.utils

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.leic52dg17.chimp.ChIMPTestApplication

@Suppress("unused")
class ChIMPInstrumentationTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application {
        return super.newApplication(cl, ChIMPTestApplication::class.java.name, context)
    }
}