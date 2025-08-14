package com.joshayoung.notemark.note.presentation.settings

import com.joshayoung.notemark.note.domain.models.SyncInterval

data class SettingsState(
    val interval: SyncInterval = SyncInterval.MANUAL
)
