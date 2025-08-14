package com.joshayoung.notemark
import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.joshayoung.notemark.auth.data.repository.AuthRepositoryImpl
import com.joshayoung.notemark.auth.domain.repository.AuthRepository
import com.joshayoung.notemark.auth.presentation.log_in.LoginViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import com.joshayoung.notemark.auth.presentation.registration.RegistrationViewModel
import com.joshayoung.notemark.core.data.DataStorageImpl
import com.joshayoung.notemark.core.data.networking.HttpClientProvider
import com.joshayoung.notemark.note.data.database.RoomLocalDataSource
import com.joshayoung.notemark.note.data.database.NoteDatabase
import com.joshayoung.notemark.auth.data.use_cases.EmailValidator
import com.joshayoung.notemark.core.AndroidConnectivityObserver
import com.joshayoung.notemark.core.ConnectivityObserver
import com.joshayoung.notemark.core.domain.DataStorage
import com.joshayoung.notemark.core.navigation.DefaultNavigator
import com.joshayoung.notemark.core.navigation.Destination
import com.joshayoung.notemark.core.navigation.Navigator
import com.joshayoung.notemark.note.data.network.KtorRemoteDataSource
import com.joshayoung.notemark.note.data.repository.NoteRepositoryImpl
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import com.joshayoung.notemark.note.domain.use_cases.PatternValidator
import com.joshayoung.notemark.note.domain.use_cases.ValidateEmail
import com.joshayoung.notemark.note.domain.use_cases.ValidatePassword
import com.joshayoung.notemark.note.domain.use_cases.ValidateUsername
import com.joshayoung.notemark.note.network.RemoteDataSource
import com.joshayoung.notemark.note.presentation.add_note.AddNoteViewModel
import com.joshayoung.notemark.note.presentation.note_detail.NoteDetailViewModel
import com.joshayoung.notemark.note.presentation.note_list.NoteListViewModel
import com.joshayoung.notemark.note.presentation.settings.SettingsViewModel
import com.joshayoung.notemark.note.presentation.start.GettingStartedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication

var appModule = module {

    single<Navigator> {
        DefaultNavigator(startDestination = Destination.AuthGraph)
    }

    viewModelOf(::MainViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::GettingStartedViewModel)

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

    singleOf(::NoteRepositoryImpl).bind<NoteRepository>()
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    singleOf(::EmailValidator).bind<PatternValidator>()
    singleOf(::ValidateUsername)
    singleOf(::ValidatePassword)
    singleOf(::ValidateEmail)

    singleOf(::DataStorageImpl).bind<DataStorage>()

    viewModelOf(::NoteListViewModel)
    viewModelOf(::AddNoteViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::NoteDetailViewModel)

    single {
        Room.databaseBuilder(
            androidApplication(),
            NoteDatabase::class.java,
            NoteDatabase.DATABASE_NAME
        ).build()
    }

    single { get<NoteDatabase>().noteDao }

    singleOf(::RoomLocalDataSource).bind<LocalDataSource>()
    singleOf(::KtorRemoteDataSource).bind<RemoteDataSource>()

    single<CoroutineScope> {
        (androidApplication() as NoteMarkApp).applicationScope
    }

    singleOf(::AndroidConnectivityObserver).bind<ConnectivityObserver>()
}