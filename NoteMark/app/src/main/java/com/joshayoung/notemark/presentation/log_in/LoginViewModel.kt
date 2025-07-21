package com.joshayoung.notemark.presentation.log_in

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.domain.NoteMarkRepository
import com.joshayoung.notemark.presentation.registration.RegistrationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val noteMarkRepository: NoteMarkRepository
) : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when(action) {
            LoginAction.OnLoginClick -> {
                viewModelScope.launch {
                    val result = noteMarkRepository.login(
                        state.username.text.toString(),
                        state.password.text.toString()
                    )

                    if (result.success) {
                        eventChannel.send(LoginEvent.Success)
                    }
                }
            }
        }
    }
}