package com.joshayoung.notemark.note.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.joshayoung.notemark.note.data.database.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert
    suspend fun upsertNote(note: NoteEntity): Long

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("DELETE from notes WHERE id = :id")
    suspend fun deleteNote(id: Long): Int

    @Query("SELECT * FROM notes WHERE id=:id")
    suspend fun getNoteById(id: Long): NoteEntity?

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
}