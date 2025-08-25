package com.joshayoung.notemark.note.domain.repository

import com.joshayoung.notemark.core.data.networking.DataError
import com.joshayoung.notemark.core.data.networking.Result
import com.joshayoung.notemark.note.data.database.entity.SyncEntity
import com.joshayoung.notemark.note.data.database.entity.SyncOperation
import com.joshayoung.notemark.note.domain.models.Note
import kotlinx.coroutines.flow.Flow

interface SyncRepository {
    suspend fun addOrUpdateQueue(
        note: Note,
        operation: SyncOperation,
    ): Result<Unit, DataError.Local>

    suspend fun clearSyncQueue()

    fun hasPendingSyncs(): Flow<Boolean>

    fun getAllSyncs(): Flow<List<SyncEntity>>

    suspend fun deleteSync(id: Long?)
}