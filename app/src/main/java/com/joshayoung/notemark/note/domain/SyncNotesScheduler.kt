package com.joshayoung.notemark.note.domain

import kotlin.time.Duration

interface SyncNotesScheduler {
    suspend fun scheduleSync(interval: Duration)

    suspend fun cancelSyncs()
}