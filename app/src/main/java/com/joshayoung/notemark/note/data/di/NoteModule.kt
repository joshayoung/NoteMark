package com.joshayoung.notemark.note.data.di

import androidx.room.Room
import com.joshayoung.notemark.note.data.SyncNoteWorkerScheduler
import com.joshayoung.notemark.note.data.database.NoteDatabase
import com.joshayoung.notemark.note.data.database.RoomLocalDataSource
import com.joshayoung.notemark.note.data.database.RoomSyncLocalDataSource
import com.joshayoung.notemark.note.data.database.SyncDatabase
import com.joshayoung.notemark.note.data.network.KtorRemoteDataSource
import com.joshayoung.notemark.note.data.repository.NoteRepositoryImpl
import com.joshayoung.notemark.note.data.repository.SyncRepositoryImpl
import com.joshayoung.notemark.note.data.use_cases.ClearLocalDataUseCase
import com.joshayoung.notemark.note.data.use_cases.DeleteEmptyNoteUseCase
import com.joshayoung.notemark.note.data.use_cases.LoggedInUserAbbreviationUseCase
import com.joshayoung.notemark.note.data.use_cases.PullRemoteNotesUseCase
import com.joshayoung.notemark.note.data.use_cases.SetSyncIntervalUseCase
import com.joshayoung.notemark.note.data.use_cases.SetUserIdUseCase
import com.joshayoung.notemark.note.data.use_cases.SyncNotesUseCase
import com.joshayoung.notemark.note.data.use_cases.UpdateSyncQueueUseCase
import com.joshayoung.notemark.note.data.workers.DataSyncWorker
import com.joshayoung.notemark.note.domain.SyncNotesScheduler
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import com.joshayoung.notemark.note.domain.database.LocalSyncDataSource
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import com.joshayoung.notemark.note.domain.repository.SyncRepository
import com.joshayoung.notemark.note.network.RemoteDataSource
import com.joshayoung.notemark.note.presentation.add_note.AddNoteViewModel
import com.joshayoung.notemark.note.presentation.note_detail.NoteDetailViewModel
import com.joshayoung.notemark.note.presentation.note_list.NoteListViewModel
import com.joshayoung.notemark.note.presentation.settings.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

var noteModule =
    module {
        singleOf(::NoteRepositoryImpl).bind<NoteRepository>()
        singleOf(::SyncRepositoryImpl).bind<SyncRepository>()
        viewModelOf(::NoteListViewModel)
        viewModelOf(::AddNoteViewModel)
        viewModelOf(::SettingsViewModel)
        viewModelOf(::NoteDetailViewModel)

        single {
            Room
                .databaseBuilder(
                    androidApplication(),
                    NoteDatabase::class.java,
                    NoteDatabase.DATABASE_NAME,
                ).build()
        }

        single {
            Room
                .databaseBuilder(
                    androidApplication(),
                    SyncDatabase::class.java,
                    SyncDatabase.DATABASE_NAME,
                ).build()
        }

        single { get<NoteDatabase>().noteDao }
        single { get<SyncDatabase>().syncDao }
        singleOf(::RoomLocalDataSource).bind<LocalDataSource>()
        singleOf(::RoomSyncLocalDataSource).bind<LocalSyncDataSource>()
        singleOf(::KtorRemoteDataSource).bind<RemoteDataSource>()
        singleOf(::SyncNotesUseCase)
        singleOf(::PullRemoteNotesUseCase)
        singleOf(::LoggedInUserAbbreviationUseCase)
        singleOf(::SetUserIdUseCase)
        singleOf(::ClearLocalDataUseCase)
        singleOf(::UpdateSyncQueueUseCase)
        singleOf(::SetSyncIntervalUseCase)
        singleOf(::DeleteEmptyNoteUseCase)

        // worker:
        workerOf(::DataSyncWorker)
        singleOf(::SyncNoteWorkerScheduler).bind<SyncNotesScheduler>()
    }