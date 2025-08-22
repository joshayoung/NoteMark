package com.joshayoung.notemark.note.domain

import kotlin.time.Duration

interface SyncNotesScheduler {
    suspend fun scheduleSync(interval: Duration)

    suspend fun cancelSyncs()

    sealed interface SyncType {
        data class NotesSync(
            val interval: Duration,
        )
    }
}