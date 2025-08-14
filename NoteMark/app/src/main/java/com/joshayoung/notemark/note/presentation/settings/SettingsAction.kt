package com.joshayoung.notemark.note.presentation.settings

import com.joshayoung.notemark.note.domain.models.SyncInterval


sealed interface SettingsAction {
    data object OnLogoutClick: SettingsAction
    data object NavigateBack: SettingsAction
    data class SetSyncInterval(val interval: SyncInterval): SettingsAction
    data object Sync: SettingsAction
}
