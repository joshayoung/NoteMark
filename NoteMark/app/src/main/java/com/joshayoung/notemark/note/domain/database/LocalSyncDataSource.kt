package com.joshayoung.notemark.note.domain.database

interface LocalSyncDataSource {
    suspend fun clearSyncQueue()
}