package com.joshayoung.notemark.note.presentation.note_landing


sealed interface NoteLandingAction {
    data object OnLogoutClick: NoteLandingAction
    data class OnDeleteClick(val id: Int): NoteLandingAction
}