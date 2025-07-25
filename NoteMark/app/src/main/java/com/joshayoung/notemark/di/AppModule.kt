package com.joshayoung.notemark.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.joshayoung.notemark.MainViewModel
import com.joshayoung.notemark.data.use_cases.EmailValidator
import com.joshayoung.notemark.data.HttpClientProvider
import com.joshayoung.notemark.data.repository.NoteMarkRepositoryImpl
import com.joshayoung.notemark.data.SessionStorageImpl
import com.joshayoung.notemark.domain.repository.NoteMarkRepository
import com.joshayoung.notemark.domain.use_cases.PatternValidator
import com.joshayoung.notemark.domain.SessionStorage
import com.joshayoung.notemark.domain.use_cases.ValidateEmail
import com.joshayoung.notemark.domain.use_cases.ValidatePassword
import com.joshayoung.notemark.domain.use_cases.ValidateUsername
import com.joshayoung.notemark.presentation.log_in.LoginViewModel
import com.joshayoung.notemark.presentation.note_landing.NoteLandingViewModel
import com.joshayoung.notemark.presentation.registration.RegistrationViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

var appModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)

    single {
        HttpClientProvider(get()).provide()
    }

    singleOf(::NoteMarkRepositoryImpl).bind<NoteMarkRepository>()
    singleOf(::EmailValidator).bind<PatternValidator>()
    singleOf(::ValidateUsername)
    singleOf(::ValidatePassword)
    singleOf(::ValidateEmail)

    singleOf(::SessionStorageImpl).bind<SessionStorage>()
    single<SharedPreferences> {
        EncryptedSharedPreferences(
            androidApplication(),
            "notemark_preferences",
            MasterKey(androidApplication()),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    viewModelOf(::NoteLandingViewModel)
}
