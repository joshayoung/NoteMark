package com.joshayoung.notemark.data

import com.joshayoung.notemark.data.database.dao.NoteDao
import com.joshayoung.notemark.data.database.entity.NoteEntity
import com.joshayoung.notemark.domain.database.LocalDataSource
import com.joshayoung.notemark.domain.models.Result
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