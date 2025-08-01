package com.joshayoung.notemark.core.data.database.dao


import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.joshayoung.notemark.core.data.database.entity.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert
    suspend fun upsertNote(note: NoteEntity)

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("DELETE FROM notes WHERE id=:id")
    suspend fun deleteNote(id: String)
}