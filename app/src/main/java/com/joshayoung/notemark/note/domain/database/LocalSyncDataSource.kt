package com.joshayoung.notemark.note.domain.database

import com.joshayoung.notemark.core.data.networking.DataError
import com.joshayoung.notemark.core.data.networking.Result
import com.joshayoung.notemark.note.data.database.entity.SyncEntity
import com.joshayoung.notemark.note.data.database.entity.SyncOperation
import com.joshayoung.notemark.note.domain.models.Note
import kotlinx.coroutines.flow.Flow

interface LocalSyncDataSource {
    suspend fun clearSyncQueue()

    suspend fun addOrUpdateQueue(
        note: Note,
        operation: SyncOperation,
    ): Result<Unit, DataError.Local>

    fun getAllSyncs(): Flow<List<SyncEntity>>

    fun hasPendingSyncs(): Flow<Boolean>

    suspend fun getSync(note: Note): SyncEntity?

    suspend fun deleteSync(id: Long?)
}