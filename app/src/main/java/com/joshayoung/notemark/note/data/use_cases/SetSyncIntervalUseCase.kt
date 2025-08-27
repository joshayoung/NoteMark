package com.joshayoung.notemark.note.data.use_cases

import com.joshayoung.notemark.core.domain.NoteDataStorage
import com.joshayoung.notemark.note.data.SyncNoteWorkerScheduler
import com.joshayoung.notemark.note.domain.models.SyncInterval
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class SetSyncIntervalUseCase(
    private val noteDataStorage: NoteDataStorage,
    private val syncNoteWorkerScheduler: SyncNoteWorkerScheduler,
) {
    suspend operator fun invoke(syncInterval: SyncInterval) {
//        noteDataStorage.saveSyncInterval(syncInterval)
        val interval =
            when (syncInterval) {
                SyncInterval.FIFTEEN -> 15.minutes
                SyncInterval.THIRTY -> 30.minutes
                SyncInterval.HOUR -> 1.hours
                else -> 30.minutes
            }

        syncNoteWorkerScheduler.scheduleSync(
            interval = interval,
        )
    }
}