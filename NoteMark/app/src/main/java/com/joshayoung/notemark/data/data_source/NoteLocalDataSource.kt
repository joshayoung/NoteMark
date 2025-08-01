package com.joshayoung.notemark.data.data_source

import com.joshayoung.notemark.data.database.NoteEntity
import com.joshayoung.notemark.domain.database.LocalDataSource
import kotlinx.coroutines.flow.Flow

import com.joshayoung.notemark.data.data_source.NoteDao
import com.joshayoung.notemark.domain.models.Note
import com.joshayoung.notemark.domain.models.Result

class NoteLocalDataSource(
    private val noteDao: NoteDao
) : LocalDataSource {
    override fun getNotes(): Flow<List<NoteEntity>> {
        return noteDao.getNotes()
    }

    override suspend fun upsertNotes(note: NoteEntity): Result {
        noteDao.upsertNote(note)

        return Result(success = true)
    }
}