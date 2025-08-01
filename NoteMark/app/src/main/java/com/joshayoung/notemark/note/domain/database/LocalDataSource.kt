package com.joshayoung.notemark.note.domain.database

import com.joshayoung.notemark.core.data.database.entity.NoteEntity
import com.joshayoung.notemark.core.domain.Result
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getNotes(): Flow<List<NoteEntity>>
    suspend fun upsertNotes(note: NoteEntity) : Result
}