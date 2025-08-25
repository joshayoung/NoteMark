package com.joshayoung.notemark.note.data.use_cases

import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.repository.NoteRepository

class DeleteEmptyNoteUseCase(
    private val noteRepository: NoteRepository,
) {
    suspend operator fun invoke(currentNote: Note?) {
        if (currentNote?.title == "" && currentNote.content == "") {
            noteRepository.deleteNote(currentNote)
        }
    }
}