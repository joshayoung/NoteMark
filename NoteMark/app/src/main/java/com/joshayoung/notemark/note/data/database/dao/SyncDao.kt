package com.joshayoung.notemark.note.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.joshayoung.notemark.note.data.database.entity.NoteEntity
import com.joshayoung.notemark.note.data.database.entity.SyncRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncDao {
    @Query("DELETE FROM sync_queue")
    suspend fun deleteAllSyncs()

    @Upsert
    suspend fun upsertNote(syncRecord: SyncRecord) : Long

    @Query("SELECT * FROM sync_queue")
    fun getAllSyncs(): Flow<List<SyncRecord>>

    @Query("SELECT * FROM sync_queue WHERE id=:id")
    suspend fun getSyncById(id: Long?) : SyncRecord?
}