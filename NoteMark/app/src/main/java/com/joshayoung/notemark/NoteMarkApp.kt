package com.joshayoung.notemark

import android.app.Application
import com.joshayoung.notemark.di.appModule
import org.koin.core.context.startKoin

class NoteMarkApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(
                appModule
            )
        }
    }
}