package com.joshayoung.notemark.auth.data.di

import com.joshayoung.notemark.auth.data.repository.AuthRepositoryImpl
import com.joshayoung.notemark.auth.data.use_cases.EmailValidator
import com.joshayoung.notemark.auth.data.use_cases.SaveAuthDataUseCase
import com.joshayoung.notemark.auth.data.use_cases.ValidateEmailUseCase
import com.joshayoung.notemark.auth.data.use_cases.ValidatePasswordUseCase
import com.joshayoung.notemark.auth.data.use_cases.ValidateUsernameUseCase
import com.joshayoung.notemark.auth.domain.repository.AuthRepository
import com.joshayoung.notemark.auth.domain.use_cases.PatternValidator
import com.joshayoung.notemark.auth.presentation.log_in.LoginViewModel
import com.joshayoung.notemark.auth.presentation.registration.RegistrationViewModel
import com.joshayoung.notemark.note.presentation.start.GettingStartedViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

var authModule =
    module {
        viewModelOf(::GettingStartedViewModel)
        viewModelOf(::LoginViewModel)
        viewModelOf(::RegistrationViewModel)
        singleOf(::AuthRepositoryImpl).bind<AuthRepository>()

        singleOf(::EmailValidator).bind<PatternValidator>()
        singleOf(::ValidateUsernameUseCase)
        singleOf(::ValidatePasswordUseCase)
        singleOf(::ValidateEmailUseCase)
        singleOf(::SaveAuthDataUseCase)
    }