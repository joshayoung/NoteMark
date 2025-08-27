package com.joshayoung.notemark.auth.data.di

import UserPreferences
import UserPreferencesSerializer
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
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
import com.joshayoung.notemark.core.data.AuthDataStorageImpl
import com.joshayoung.notemark.core.data.NoteDataStorageImpl
import com.joshayoung.notemark.core.domain.AuthDataStorage
import com.joshayoung.notemark.core.domain.NoteDataStorage
import com.joshayoung.notemark.note.presentation.start.GettingStartedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun createDataStore(context: Context): DataStore<UserPreferences> =
    DataStoreFactory.create(
        serializer = UserPreferencesSerializer,
        produceFile = { context.dataStoreFile("auth_storage") },
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        corruptionHandler =
            ReplaceFileCorruptionHandler<UserPreferences>(
                produceNewData = { UserPreferences() },
            ),
    )

fun createPreferencesDataStore(context: Context): DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("note_storage") },
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        corruptionHandler =
            ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() },
            ),
    )

var authModule =
    module {

        single { createDataStore(get()) }
        single { createPreferencesDataStore(get()) }

        singleOf(::AuthDataStorageImpl).bind<AuthDataStorage>()
        singleOf(::NoteDataStorageImpl).bind<NoteDataStorage>()

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
