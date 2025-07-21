package com.joshayoung.notemark.di

import com.joshayoung.notemark.data.EmailValidator
import com.joshayoung.notemark.data.HttpClientProvider
import com.joshayoung.notemark.data.NoteMarkRepositoryImpl
import com.joshayoung.notemark.domain.NoteMarkRepository
import com.joshayoung.notemark.domain.PatternValidator
import com.joshayoung.notemark.domain.use_cases.ValidateEmail
import com.joshayoung.notemark.domain.use_cases.ValidatePassword
import com.joshayoung.notemark.domain.use_cases.ValidateUsername
import com.joshayoung.notemark.presentation.log_in.LoginViewModel
import com.joshayoung.notemark.presentation.registration.RegistrationViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

var appModule = module {
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)

    single {
        HttpClientProvider().provide()
    }

    singleOf(::NoteMarkRepositoryImpl).bind<NoteMarkRepository>()
    singleOf(::EmailValidator).bind<PatternValidator>()
    singleOf(::ValidateUsername)
    singleOf(::ValidatePassword)
    singleOf(::ValidateEmail)
}
