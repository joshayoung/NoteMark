package com.joshayoung.notemark.note.data.database

import com.joshayoung.notemark.core.domain.DataStorage
import com.joshayoung.notemark.core.domain.util.DataError
import com.joshayoung.notemark.core.domain.util.Result
import com.joshayoung.notemark.note.data.database.dao.SyncDao
import com.joshayoung.notemark.note.data.database.entity.SyncOperation
import com.joshayoung.notemark.note.data.database.entity.SyncRecord
import com.joshayoung.notemark.note.data.mappers.toNoteEntity
import com.joshayoung.notemark.note.domain.database.LocalSyncDataSource
import com.joshayoung.notemark.note.domain.models.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.flatten
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.transform
import kotlinx.serialization.json.Json

class RoomSyncLocalDataSource(
    private val dataStorage: DataStorage,
    private val syncDao: SyncDao
) : LocalSyncDataSource {
    override suspend fun clearSyncQueue() {
        syncDao.deleteAllSyncs()
    }

    override suspend fun addOrUpdateQueue(note: Note, operation: SyncOperation) : Result<Unit, DataError.Local> {
        val user = dataStorage.getUserid()
        if (user == null) {
            // TODO: Update this error:
            return Result.Error(error = DataError.Local.DISK_FULL)
        }

        val noteEntity = note.toNoteEntity()
        val syncRecord = SyncRecord(
            userId = user,
            noteId = note.remoteId,
            operation = operation,
            payload = Json.encodeToString(noteEntity),
            timestamp = System.currentTimeMillis()
        )

        // move to Use Case:
        if (operation == SyncOperation.CREATE)
        {
            syncDao.deleteAllSyncsForNoteId(note.remoteId.toString(), SyncOperation.CREATE.name)
        }

        if (operation == SyncOperation.UPDATE)
        {
            // This way I only ever have i update per note.
            // This should not be the remote id here:
            syncDao.deleteAllSyncsForNoteId(note.remoteId.toString(), SyncOperation.UPDATE.name)
        }
        syncDao.upsertSync(syncRecord)

        // TODO: Re-evaluate this return value
        return Result.Success(Unit)
    }

    override fun getAllSyncs(): Flow<List<SyncRecord>> {
        val syncs = syncDao.getAllSyncs()

        return syncs
    }

    override fun hasPendingSyncs(): Flow<Boolean> {
        return syncDao.getAllSyncs().map { itemList ->
            itemList.isNotEmpty()
        }
    }

    override suspend fun getSync(note: Note): SyncRecord? {
        val sync = syncDao.getSyncById(note.id)

        return sync
    }

    override suspend fun deleteSync(id: Long?) {
        syncDao.deleteSync(id)
    }
}