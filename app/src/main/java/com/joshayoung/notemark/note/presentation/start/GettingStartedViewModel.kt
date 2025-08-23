package com.joshayoung.notemark.note.presentation.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.core.navigation.Destination
import com.joshayoung.notemark.core.navigation.Navigator
import kotlinx.coroutines.launch

class GettingStartedViewModel(
    private val navigator: Navigator,
) : ViewModel() {
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