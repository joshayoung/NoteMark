package com.joshayoung.notemark.note.domain.database

import com.joshayoung.notemark.core.domain.util.DataError
import com.joshayoung.notemark.core.domain.util.EmptyResult
import com.joshayoung.notemark.core.domain.util.Result
import com.joshayoung.notemark.note.data.database.entity.NoteEntity
import com.joshayoung.notemark.note.domain.models.Note
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getNotes(): Flow<List<Note>>
     suspend fun upsertNote(note: Note): Result<Int?, DataError.Local>
    suspend fun getNote(id: Int) : Note?
    suspend fun deleteNote(id: Int)
}