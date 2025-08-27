package com.joshayoung.notemark.note.data.database

import com.joshayoung.notemark.core.data.networking.DataError
import com.joshayoung.notemark.core.data.networking.Result
import com.joshayoung.notemark.core.domain.AuthDataStorage
import com.joshayoung.notemark.core.domain.NoteDataStorage
import com.joshayoung.notemark.note.data.database.dao.SyncDao
import com.joshayoung.notemark.note.data.database.entity.SyncEntity
import com.joshayoung.notemark.note.data.database.entity.SyncOperation
import com.joshayoung.notemark.note.data.mappers.toNoteEntity
import com.joshayoung.notemark.note.domain.database.LocalSyncDataSource
import com.joshayoung.notemark.note.domain.models.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class RoomSyncLocalDataSource(
    private val authDataStorage: AuthDataStorage,
    private val noteDataStorage: NoteDataStorage,
    private val syncDao: SyncDao,
) : LocalSyncDataSource {
    override suspend fun clearSyncQueue() {
        syncDao.deleteAllSyncs()
    }

    override suspend fun addOrUpdateQueue(
        note: Note,
        operation: SyncOperation,
    ): Result<Unit, DataError.Local> {
//        val user = noteDataStorage.getUserid()
        val user = ""
        if (user == null) {
            // TODO: Update this error:
            return Result.Error(error = DataError.Local.DISK_FULL)
        }

        val noteEntity = note.toNoteEntity()
        val syncEntity =
            SyncEntity(
                userId = user,
                noteId = note.remoteId,
                operation = operation,
                payload = Json.encodeToString(noteEntity),
                timestamp = System.currentTimeMillis(),
            )

        // move to Use Case:
        if (operation == SyncOperation.CREATE) {
            syncDao.deleteAllSyncsForNoteId(note.remoteId.toString(), SyncOperation.CREATE.name)
        }

        if (operation == SyncOperation.UPDATE) {
            // This way I only ever have i update per note.
            // This should not be the remote id here:
            syncDao.deleteAllSyncsForNoteId(note.remoteId.toString(), SyncOperation.UPDATE.name)
        }
        syncDao.upsertSync(syncEntity)

        // TODO: Re-evaluate this return value
        return Result.Success(Unit)
    }

    override fun getAllSyncs(): Flow<List<SyncEntity>> {
        val syncs = syncDao.getAllSyncs()

        return syncs
    }

    override fun hasPendingSyncs(): Flow<Boolean> =
        syncDao.getAllSyncs().map { itemList ->
            itemList.isNotEmpty()
        }

    override suspend fun getSync(note: Note): SyncEntity? {
        val sync = syncDao.getSyncById(note.id)

        return sync
    }

    override suspend fun deleteSync(id: Long?) {
        syncDao.deleteSync(id)
    }
}