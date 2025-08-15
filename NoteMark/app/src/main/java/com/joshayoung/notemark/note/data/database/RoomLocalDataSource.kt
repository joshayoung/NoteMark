package com.joshayoung.notemark.note.data.database

import android.database.sqlite.SQLiteFullException
import com.joshayoung.notemark.core.domain.util.DataError
import com.joshayoung.notemark.core.domain.util.Result
import com.joshayoung.notemark.note.data.database.dao.NoteDao
import com.joshayoung.notemark.note.data.mappers.toNote
import com.joshayoung.notemark.note.data.mappers.toNoteEntity
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import com.joshayoung.notemark.note.domain.models.Note
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

    override suspend fun upsertNote(note: Note): Result<Int?, DataError.Local> {
        try {
            val noteEntity = note.toNoteEntity()
            val id = noteDao.upsertNote(noteEntity)
            return Result.Success(data = noteEntity.id)
        } catch(e: SQLiteFullException) {
            return Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun getNote(id: Int) : Note? {
        val noteEntity = noteDao.getNoteById(id)?.toNote()

        return noteEntity
    }

    override suspend fun deleteNote(id: Int) : Boolean {
        val rows = noteDao.deleteNote(id)

        return rows == 1
    }

    override suspend fun removeAllNotes() {
        noteDao.deleteAllNotes()
    }
}