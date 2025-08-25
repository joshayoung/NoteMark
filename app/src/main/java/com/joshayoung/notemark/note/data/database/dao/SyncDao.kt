package com.joshayoung.notemark.note.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.joshayoung.notemark.note.data.database.entity.SyncEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SyncDao {
    @Query("DELETE FROM sync_queue")
    suspend fun deleteAllSyncs()

    @Upsert
    suspend fun upsertSync(syncEntity: SyncEntity): Long

    @Query("SELECT * FROM sync_queue")
    fun getAllSyncs(): Flow<List<SyncEntity>>

    @Query("SELECT * FROM sync_queue WHERE noteId = :id")
    fun getAllByNoteId(id: String): List<SyncEntity>

    @Query("SELECT * FROM sync_queue WHERE id=:id")
    suspend fun getSyncById(id: Long?): SyncEntity?

    @Query("DELETE from sync_queue WHERE id = :id")
    suspend fun deleteSync(id: Long?): Int

    @Query("DELETE from sync_queue WHERE noteId = :id AND operation = :operation")
    suspend fun deleteAllSyncsForNoteId(
        id: String,
        operation: String,
    ): Int
}