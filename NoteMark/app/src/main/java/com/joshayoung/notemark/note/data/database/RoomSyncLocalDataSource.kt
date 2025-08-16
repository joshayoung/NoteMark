package com.joshayoung.notemark.note.data.database

import com.joshayoung.notemark.note.data.database.dao.SyncDao
import com.joshayoung.notemark.note.domain.database.LocalSyncDataSource

class RoomSyncLocalDataSource(
    private val syncDao: SyncDao
) : LocalSyncDataSource {
    override suspend fun clearSyncQueue() {
        syncDao.deleteAllSyncs()
    }
}