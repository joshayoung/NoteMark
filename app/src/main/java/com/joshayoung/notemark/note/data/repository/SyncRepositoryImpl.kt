package com.joshayoung.notemark.note.data.repository

import com.joshayoung.notemark.core.data.networking.DataError
import com.joshayoung.notemark.core.data.networking.Result
import com.joshayoung.notemark.note.data.database.entity.SyncEntity
import com.joshayoung.notemark.note.data.database.entity.SyncOperation
import com.joshayoung.notemark.note.domain.database.LocalSyncDataSource
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.repository.SyncRepository
import kotlinx.coroutines.flow.Flow

class SyncRepositoryImpl(
    private val localSyncDataSource: LocalSyncDataSource,
) : SyncRepository {
    override suspend fun addOrUpdateQueue(
        note: Note,
        operation: SyncOperation,
    ): Result<Unit, DataError.Local> = localSyncDataSource.addOrUpdateQueue(note, operation)

    override suspend fun clearSyncQueue() {
        localSyncDataSource.clearSyncQueue()
    }

    override fun hasPendingSyncs(): Flow<Boolean> = localSyncDataSource.hasPendingSyncs()

    override fun getAllSyncs(): Flow<List<SyncEntity>> = localSyncDataSource.getAllSyncs()

    override suspend fun deleteSync(id: Long?) {
        localSyncDataSource.deleteSync(id)
    }
}