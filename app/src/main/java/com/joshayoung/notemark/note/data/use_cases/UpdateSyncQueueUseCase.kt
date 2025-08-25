package com.joshayoung.notemark.note.data.use_cases

import com.joshayoung.notemark.note.data.database.entity.SyncOperation
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.repository.SyncRepository

class UpdateSyncQueueUseCase(
    private val syncRepository: SyncRepository,
) {
    suspend operator fun invoke(
        currentNote: Note?,
        inEditMode: Boolean,
    ) {
        currentNote?.let { note ->
            if (inEditMode) {
                syncRepository.addOrUpdateQueue(note, SyncOperation.UPDATE)

                // TODO: Does this return properly
                return
            }

            syncRepository.addOrUpdateQueue(note, SyncOperation.CREATE)
        }
    }
}