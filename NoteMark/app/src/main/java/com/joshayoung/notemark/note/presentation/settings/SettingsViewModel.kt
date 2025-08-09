package com.joshayoung.notemark.note.presentation.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.auth.data.repository.AuthRepositoryImpl
import com.joshayoung.notemark.core.domain.DataStorage
import kotlinx.coroutines.launch

class SettingsViewModel(
    val authRepositoryImpl: AuthRepositoryImpl
) : ViewModel() {
    var state by mutableStateOf(SettingsState())
        private set

    fun onAction(action : SettingsAction) {
        when(action) {
            SettingsAction.OnLogoutClick -> {
                viewModelScope.launch {
                    authRepositoryImpl.logout()
                }
            }
        }
    }
}