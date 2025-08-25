package com.joshayoung.notemark.note.data.use_cases

import com.joshayoung.notemark.core.data.networking.Result
import com.joshayoung.notemark.note.data.network.KtorRemoteDataSource
import com.joshayoung.notemark.note.domain.database.LocalDataSource

class PullRemoteNotesUseCase(
    private val remoteDataSource: KtorRemoteDataSource,
    private val localDataSource: LocalDataSource,
) {
    suspend operator fun invoke() {
        val remoteNotes = remoteDataSource.getNotes()
        if (remoteNotes is Result.Success) {
            val notes = remoteNotes.data
            localDataSource.removeAllNotes()
            localDataSource.addRemoteNotes(notes)
        }
    }
}