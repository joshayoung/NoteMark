package com.joshayoung.notemark.core.data.di

import com.joshayoung.notemark.core.data.AndroidConnectivityObserver
import com.joshayoung.notemark.core.data.networking.HttpClientProvider
import com.joshayoung.notemark.core.domain.ConnectivityObserver
import com.joshayoung.notemark.core.domain.use_cases.NoteMarkUseCases
import com.joshayoung.notemark.core.navigation.DefaultNavigator
import com.joshayoung.notemark.core.navigation.Destination
import com.joshayoung.notemark.core.navigation.Navigator
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

//        singleOf(::DataStorageImpl).bind<DataStorage>()

        singleOf(::NoteMarkUseCases)
    }