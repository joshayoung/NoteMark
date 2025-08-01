package com.joshayoung.notemark.data.data_source


import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.joshayoung.notemark.data.database.NoteEntity
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