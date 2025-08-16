package com.joshayoung.notemark.note.data.database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface SyncDao {
    @Query("DELETE FROM sync_queue")
    suspend fun deleteAllSyncs()
}