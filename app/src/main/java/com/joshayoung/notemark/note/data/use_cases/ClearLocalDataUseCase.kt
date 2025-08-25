package com.joshayoung.notemark.note.data.use_cases

import com.joshayoung.notemark.note.data.SyncNoteWorkerScheduler
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import com.joshayoung.notemark.note.domain.repository.SyncRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ClearLocalDataUseCase(
    private val syncNoteWorkerScheduler: SyncNoteWorkerScheduler,
    private val syncRepository: SyncRepository,
    private val noteRepository: NoteRepository,
    private val applicationScope: CoroutineScope,
) {
    operator fun invoke() {
        applicationScope.launch {
            syncNoteWorkerScheduler.cancelSyncs()
            syncRepository.clearSyncQueue()
            noteRepository.removeAllNotes()
        }
    }
}