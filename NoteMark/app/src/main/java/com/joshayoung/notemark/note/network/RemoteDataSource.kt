package com.joshayoung.notemark.note.network
import com.joshayoung.notemark.core.domain.models.Result

import com.joshayoung.notemark.note.domain.models.Note

interface RemoteDataSource {
    suspend fun saveNote(note: Note) : Result
    suspend fun getNotes() : Result
}