package com.joshayoung.notemark.presentation.note_landing


sealed interface NoteLandingAction {
    data object OnLogoutClick: NoteLandingAction
}