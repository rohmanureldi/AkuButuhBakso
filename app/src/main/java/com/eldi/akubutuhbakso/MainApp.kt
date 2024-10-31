package com.eldi.akubutuhbakso

import android.app.Application
import com.eldi.akubutuhbakso.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApp)
            androidLogger(Level.DEBUG)
            modules(appModule)
        }
    }
}
