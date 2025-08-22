package com.joshayoung.notemark.note.presentation.start

sealed interface StartAction {
    data object CreateAccount : StartAction

    data object Login : StartAction
}
