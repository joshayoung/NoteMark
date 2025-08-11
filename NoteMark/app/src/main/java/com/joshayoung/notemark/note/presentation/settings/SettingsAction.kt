package com.joshayoung.notemark.note.presentation.settings


sealed interface SettingsAction {
    data object OnLogoutClick: SettingsAction
    data object NavigateBack: SettingsAction
}
