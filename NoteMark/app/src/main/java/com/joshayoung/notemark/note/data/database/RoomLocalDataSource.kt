package com.joshayoung.notemark.note.data.database

import com.joshayoung.notemark.core.domain.models.Result
import com.joshayoung.notemark.core.utils.getTimeStampForInsert
import com.joshayoung.notemark.note.data.database.dao.NoteDao
import com.joshayoung.notemark.note.data.database.entity.NoteEntity
import com.joshayoung.notemark.note.data.mappers.toNote
import com.joshayoung.notemark.note.data.mappers.toNoteEntity
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.presentation.note_landing.mappers.toNoteUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalDataSource(
    private val noteDao: NoteDao
) : LocalDataSource {
    override fun getNotes(): Flow<List<Note>> {
        val entities = noteDao.getNotes();

        return entities.map { noteEntities ->
            noteEntities.map {
                it.toNote()
            }
        }
    }

    override suspend fun upsertNote(note: Note): Result {
        val noteEntity = note.toNoteEntity()
        noteDao.upsertNote(noteEntity)

        return Result(success = true)
    }

    override suspend fun getNote(id: Int) : Note? {
        val noteEntity = noteDao.getNoteById(id)?.toNote()

        return noteEntity
    }

    override suspend fun deleteNote(id: Int) {
        noteDao.deleteNote(id)
    }
}