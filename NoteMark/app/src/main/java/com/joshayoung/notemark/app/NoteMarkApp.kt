package com.joshayoung.notemark.app

import android.app.Application
import com.joshayoung.notemark.app.di.appModule
import com.joshayoung.notemark.auth.data.di.authModule
import com.joshayoung.notemark.core.data.di.coreModule
import com.joshayoung.notemark.note.data.di.noteModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class NoteMarkApp : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@NoteMarkApp)
            workManagerFactory()
            modules(
                appModule,
                authModule,
                coreModule,
                noteModule,
            )
        }
    }
}