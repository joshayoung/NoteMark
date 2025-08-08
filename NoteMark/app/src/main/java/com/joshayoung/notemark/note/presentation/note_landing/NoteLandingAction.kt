package com.joshayoung.notemark.note.presentation.note_landing

import com.joshayoung.notemark.note.presentation.note_landing.model.NoteUi


sealed interface NoteLandingAction {
    data object OnLogoutClick: NoteLandingAction
    data class OnDeleteClick(val noteUi: NoteUi): NoteLandingAction
}