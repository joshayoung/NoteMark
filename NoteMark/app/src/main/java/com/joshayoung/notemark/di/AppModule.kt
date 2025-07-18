package com.joshayoung.notemark.di

import com.joshayoung.notemark.data.HttpClientProvider
import com.joshayoung.notemark.data.NoteMarkRepositoryImpl
import com.joshayoung.notemark.domain.NoteMarkRepository
import com.joshayoung.notemark.presentation.registration.RegistrationViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

var appModule = module {
    viewModelOf(::RegistrationViewModel)

    single {
        HttpClientProvider().provide()
    }

    singleOf(::NoteMarkRepositoryImpl).bind<NoteMarkRepository>()
}
