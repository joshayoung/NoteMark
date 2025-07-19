package com.joshayoung.notemark.presentation.registration

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.domain.NoteMarkRepository
import com.joshayoung.notemark.domain.use_cases.ValidateEmail
import com.joshayoung.notemark.domain.use_cases.ValidatePassword
import com.joshayoung.notemark.domain.use_cases.ValidateUsername
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val noteMarkApi: NoteMarkRepository,
    private val validateUsername: ValidateUsername,
    private val validatePassword: ValidatePassword,
    private val validateEmail: ValidateEmail
) : ViewModel() {
    var state by mutableStateOf(RegistrationState())
        private set

    private val eventChannel = Channel<RegistrationEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        // TODO: Find a better way:
        combine(
            state.username.textAsFlow(),
            state.email.textAsFlow(),
            state.password.textAsFlow(),
            state.repeatedPassword.textAsFlow()
        ) {
            state = state.copy(canRegister = true)
        }.launchIn(viewModelScope)
    }

    fun TextFieldState.textAsFlow() : Flow<CharSequence> = snapshotFlow { text }

    // TODO: Improve Validation
//    fun allFieldsEntered() : Boolean {
//        return !state.username.text.isEmpty() &&
//        !state.email.text.isEmpty() &&
//        !state.password.text.isEmpty() &&
//        !state.repeatedPassword.text.isEmpty()
//    }

    fun onAction(action: RegistrationAction) {
        when(action) {
            RegistrationAction.OnRegisterClick -> {
                val usernameResult = validateUsername(state.username.text.toString())
                if (usernameResult.invalidUsername) {
                    state = state.copy(canRegister = false, usernameError = usernameResult.error)

                    return;
                }

                val emailResult = validateEmail(state.email.text.toString())
                if (emailResult.inValidEmail) {

                    state = state.copy(canRegister = false, emailError = emailResult.error)

                    return;
                }

                val passwordResult = validatePassword(
                    state.password.text.toString(),
                    state.repeatedPassword.text.toString());

                if (passwordResult.isNotEqual)
                {
                    state = state.copy(canRegister = false, passwordError = passwordResult.error)

                    return;
                }

                register()
            }
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
                // TODO: Edit the state here instead of sending a message for errors?
                eventChannel.send(RegistrationEvent.Error(result.error))
            }
        }
    }
}