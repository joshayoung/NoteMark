package com.joshayoung.notemark.note.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.core.domain.AuthDataStorage
import com.joshayoung.notemark.core.domain.NoteDataStorage
import com.joshayoung.notemark.core.navigation.Destination
import com.joshayoung.notemark.core.navigation.Navigator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class GettingStartedViewModel(
    private val navigator: Navigator,
    private val authDataStorage: AuthDataStorage,
    private val noteDataStorage: NoteDataStorage,
) : ViewModel() {
    init {
        viewModelScope.launch {
            val a = authDataStorage.username.first()
//            val b = noteDataStorage.getUserid()
        }
    }

    fun onAction(action: StartAction) {
        when (action) {
            StartAction.CreateAccount -> {
                viewModelScope.launch {
                    navigator.navigate(Destination.RegistrationScreen)
                }
            }

            StartAction.Login -> {
                viewModelScope.launch {
                    navigator.navigate(Destination.LoginScreen)
                }
            }
        }
    }
}