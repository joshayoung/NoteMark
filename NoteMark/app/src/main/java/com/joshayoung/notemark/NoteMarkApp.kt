package com.joshayoung.notemark

import android.app.Application
import com.joshayoung.notemark.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NoteMarkApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NoteMarkApp)
            modules(
                appModule
            )
        }
    }
}