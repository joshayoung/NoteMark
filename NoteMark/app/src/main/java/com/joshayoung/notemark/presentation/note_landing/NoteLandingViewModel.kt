package com.joshayoung.notemark.presentation.note_landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.domain.repository.NoteMarkRepository
import com.joshayoung.notemark.domain.SessionStorage
import kotlinx.coroutines.launch

class NoteLandingViewModel(
    private val sessionStorage: SessionStorage
) : ViewModel() {

    fun onAction(action: NoteLandingAction) {

        when(action) {
            NoteLandingAction.OnLogoutClick -> {
                viewModelScope.launch {
                    sessionStorage.set(null)
                }
            }
        }
    }
}