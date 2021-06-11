package com.example.lumenassignment.lumen_app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class LumenApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Android context
            androidContext(applicationContext)
            // modules
            modules(LumenModules().createModules(applicationContext))
        }
    }
}