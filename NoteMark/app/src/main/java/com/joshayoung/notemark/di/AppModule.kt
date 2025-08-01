package com.joshayoung.notemark.di

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.joshayoung.notemark.MainViewModel
import com.joshayoung.notemark.data.DataStorageImpl
import com.joshayoung.notemark.data.HttpClientProvider
import com.joshayoung.notemark.data.database.NoteDatabase
import com.joshayoung.notemark.data.RoomLocalDataSource
import com.joshayoung.notemark.data.repository.NoteMarkRepositoryImpl
import com.joshayoung.notemark.data.use_cases.EmailValidator
import com.joshayoung.notemark.domain.DataStorage
import com.joshayoung.notemark.domain.database.LocalDataSource
import com.joshayoung.notemark.domain.repository.NoteMarkRepository
import com.joshayoung.notemark.domain.use_cases.PatternValidator
import com.joshayoung.notemark.domain.use_cases.ValidateEmail
import com.joshayoung.notemark.domain.use_cases.ValidatePassword
import com.joshayoung.notemark.domain.use_cases.ValidateUsername
import com.joshayoung.notemark.presentation.add_note.AddNoteViewModel
import com.joshayoung.notemark.presentation.log_in.LoginViewModel
import com.joshayoung.notemark.presentation.note_landing.NoteLandingViewModel
import com.joshayoung.notemark.presentation.registration.RegistrationViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

    single {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { get<Context>().preferencesDataStoreFile("notemark_preferences") }
        )
    }

    singleOf(::NoteMarkRepositoryImpl).bind<NoteMarkRepository>()
    singleOf(::EmailValidator).bind<PatternValidator>()
    singleOf(::ValidateUsername)
    singleOf(::ValidatePassword)
    singleOf(::ValidateEmail)

    singleOf(::DataStorageImpl).bind<DataStorage>()

    viewModelOf(::NoteLandingViewModel)

    single { parameters -> AddNoteViewModel(get(), parameters.get()) }

    single {
        Room.databaseBuilder(
            androidApplication(),
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    single { get<NoteDatabase>().noteDao }

    singleOf(::RoomLocalDataSource).bind<LocalDataSource>()
}
