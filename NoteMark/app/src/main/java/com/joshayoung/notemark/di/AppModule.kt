package com.joshayoung.notemark.di

import com.joshayoung.notemark.presentation.registration.RegistrationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

var appModule = module {
    viewModelOf(::RegistrationViewModel)

}
