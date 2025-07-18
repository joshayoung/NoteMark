package com.joshayoung.notemark.presentation.registration

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.domain.NoteMarkRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val noteMarkApi: NoteMarkRepository
) : ViewModel() {
    var state by mutableStateOf(RegistrationState())
        private set

    private val eventChannel = Channel<RegistrationEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        combine(
            state.username.textAsFlow(),
            state.email.textAsFlow(),
            state.password.textAsFlow(),
            state.repeatedPassword.textAsFlow()
        ) {
            state = state.copy(canRegister = allFieldsEntered())
        }.launchIn(viewModelScope)
    }

    fun TextFieldState.textAsFlow() : Flow<CharSequence> = snapshotFlow { text }

    // TODO: Improve Validation
    fun allFieldsEntered() : Boolean {
        return !state.username.text.isEmpty() &&
        !state.email.text.isEmpty() &&
        !state.password.text.isEmpty() &&
        !state.repeatedPassword.text.isEmpty()
    }

    fun onAction(action: RegistrationAction) {
        when(action) {
            RegistrationAction.OnRegisterClick -> register()
        }
    }

    private fun register() {
        viewModelScope.launch {
            state = state.copy(isRegistering = true)

            val result = noteMarkApi.register(
                username = state.username.text.toString(),
                email = state.email.text.toString(),
                password = state.password.text.toString()
            )

            state = state.copy(isRegistering = false)

            if (result.success) {
                eventChannel.send(RegistrationEvent.RegistrationSuccess)
            } else {
                eventChannel.send(RegistrationEvent.Error(result.error))
            }
        }
    }
}