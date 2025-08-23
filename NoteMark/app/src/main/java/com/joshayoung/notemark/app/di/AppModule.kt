package com.joshayoung.notemark.app.di
import com.joshayoung.notemark.app.MainViewModel
import com.joshayoung.notemark.app.NoteMarkApp
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

var appModule =
    module {
        viewModelOf(::MainViewModel)

        single<CoroutineScope> {
            (androidApplication() as NoteMarkApp).applicationScope
        }
    }