package com.joshayoung.notemark.note.domain.database

import com.joshayoung.notemark.core.domain.util.DataError
import com.joshayoung.notemark.core.domain.util.Result
import com.joshayoung.notemark.note.data.database.entity.SyncOperation
import com.joshayoung.notemark.note.data.database.entity.SyncRecord
import com.joshayoung.notemark.note.domain.models.Note
import kotlinx.coroutines.flow.Flow

interface LocalSyncDataSource {
    suspend fun clearSyncQueue()
    suspend fun addOrUpdateQueue(note: Note, operation: SyncOperation) : Result<Unit, DataError.Local>
    fun getAllSyncs() : Flow<List<SyncRecord>>
    suspend fun getSync(note: Note): SyncRecord?
    suspend fun deleteSync(id: Long?)
}