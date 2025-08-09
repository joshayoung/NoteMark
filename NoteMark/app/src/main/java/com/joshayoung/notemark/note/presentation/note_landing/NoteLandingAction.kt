package com.joshayoung.notemark.note.presentation.note_landing

import com.joshayoung.notemark.note.presentation.note_landing.model.NoteUi


sealed interface NoteLandingAction {
    data class OnDeleteClick(val noteUi: NoteUi): NoteLandingAction
}