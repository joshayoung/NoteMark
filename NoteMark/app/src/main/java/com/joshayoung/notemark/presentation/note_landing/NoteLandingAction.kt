package com.joshayoung.notemark.presentation.note_landing


sealed interface NoteLandingAction {
    data object OnLogoutClick: NoteLandingAction
    data object OnEditClick: NoteLandingAction
    data class OnDeleteClick(val id: String): NoteLandingAction
}