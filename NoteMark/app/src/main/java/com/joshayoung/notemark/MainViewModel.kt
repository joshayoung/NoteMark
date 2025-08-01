package com.joshayoung.notemark

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.joshayoung.notemark.auth.presentation.log_in.LoginViewModel
import com.joshayoung.notemark.auth.presentation.registration.RegistrationViewModel
import com.joshayoung.notemark.core.data.DataStorageImpl
import com.joshayoung.notemark.core.data.HttpClientProvider
import com.joshayoung.notemark.core.data.RoomLocalDataSource
import com.joshayoung.notemark.core.data.database.NoteDatabase
import com.joshayoung.notemark.core.data.use_cases.EmailValidator
import com.joshayoung.notemark.core.domain.DataStorage
import com.joshayoung.notemark.note.data.repository.NoteMarkRepositoryImpl
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import com.joshayoung.notemark.note.domain.repository.NoteMarkRepository
import com.joshayoung.notemark.note.domain.use_cases.PatternValidator
import com.joshayoung.notemark.note.domain.use_cases.ValidateEmail
import com.joshayoung.notemark.note.domain.use_cases.ValidatePassword
import com.joshayoung.notemark.note.domain.use_cases.ValidateUsername
import com.joshayoung.notemark.note.presentation.add_note.AddNoteViewModel
import com.joshayoung.notemark.note.presentation.note_landing.NoteLandingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

class MainViewModel(
    private val dataStorage: DataStorage,

) : ViewModel() {
    var state by mutableStateOf(MainState())
        private set

    val authData: StateFlow<String> = dataStorage.values
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = "loading"
        )

    init {
        viewModelScope.launch {
            state = state.copy(isCheckingSession = true)
            state = state.copy(
                isAuthenticated = checkAccessToken()
            )
            state = state.copy(isCheckingSession = false)
        }

    }

    private suspend fun checkAccessToken() : Boolean {
        val token = dataStorage.getAuthData().first().accessToken

        return token != null && token != "unset"
    }
}

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
            NoteDatabase.Companion.DATABASE_NAME
        ).build()
    }

    single { get<NoteDatabase>().noteDao }

    singleOf(::RoomLocalDataSource).bind<LocalDataSource>()
}