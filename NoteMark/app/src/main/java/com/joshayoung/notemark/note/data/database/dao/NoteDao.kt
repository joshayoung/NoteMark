package com.joshayoung.notemark.note.data.database.dao


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.joshayoung.notemark.note.data.database.entity.NoteEntity
import com.joshayoung.notemark.note.domain.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert
    suspend fun upsertNote(note: NoteEntity)

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("DELETE from notes WHERE id = :id")
    suspend fun deleteNote(id: Int)

    @Query("SELECT * FROM notes WHERE id=:id")
    suspend fun getNoteById(id: Int) : NoteEntity?
}