package com.joshayoung.notemark.domain.database

import com.joshayoung.notemark.data.database.NoteEntity
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getNotes(): Flow<List<NoteEntity>>
    suspend fun upsertNotes(note: NoteEntity) : com.joshayoung.notemark.domain.models.Result
}