package com.joshayoung.notemark.core.data.di

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.joshayoung.notemark.core.AndroidConnectivityObserver
import com.joshayoung.notemark.core.ConnectivityObserver
import com.joshayoung.notemark.core.data.DataStorageImpl
import com.joshayoung.notemark.core.data.networking.HttpClientProvider
import com.joshayoung.notemark.core.domain.DataStorage
import com.joshayoung.notemark.core.domain.use_cases.NoteMarkUseCases
import com.joshayoung.notemark.core.navigation.DefaultNavigator
import com.joshayoung.notemark.core.navigation.Destination
import com.joshayoung.notemark.core.navigation.Navigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreModule =
    module {

        singleOf(::AndroidConnectivityObserver).bind<ConnectivityObserver>()

        single<Navigator> {
            DefaultNavigator(startDestination = Destination.AuthGraph)
        }
        single {
            HttpClientProvider(get()).provide()
        }

        single {
            PreferenceDataStoreFactory.create(
                corruptionHandler =
                    ReplaceFileCorruptionHandler(
                        produceNewData = { emptyPreferences() },
                    ),
                scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
                produceFile = { get<Context>().preferencesDataStoreFile("notemark_preferences") },
            )
        }

        singleOf(::DataStorageImpl).bind<DataStorage>()

        singleOf(::NoteMarkUseCases)
    }