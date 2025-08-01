package com.joshayoung.notemark.note.data.database

import com.joshayoung.notemark.core.domain.models.Result
import com.joshayoung.notemark.note.data.database.dao.NoteDao
import com.joshayoung.notemark.note.data.database.entity.NoteEntity
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import kotlinx.coroutines.flow.Flow

class RoomLocalDataSource(
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