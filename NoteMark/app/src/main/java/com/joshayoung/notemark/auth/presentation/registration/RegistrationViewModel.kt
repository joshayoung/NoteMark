package com.joshayoung.notemark.auth.presentation.registration

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.auth.domain.repository.AuthRepository
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import com.joshayoung.notemark.note.domain.use_cases.ValidateEmail
import com.joshayoung.notemark.note.domain.use_cases.ValidatePassword
import com.joshayoung.notemark.note.domain.use_cases.ValidateUsername
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val authRepository: AuthRepository,
    private val validateUsername: ValidateUsername,
    private val validatePassword: ValidatePassword,
    private val validateEmail: ValidateEmail
) : ViewModel() {
    var state by mutableStateOf(RegistrationState())
        private set

    private val eventChannel = Channel<RegistrationEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        state.username.textAsFlow()
            .onEach { username ->
                if (username == "")
                {
                    return@onEach
                }

                val usernameResult = validateUsername(state.username.text.toString())
                state = state.copy(
                    usernameError = if (usernameResult.invalidUsername) "Username must be at least 3 characters" else "",
                    invalidUsername = usernameResult.invalidUsername
                )

                state = state.copy(
                    canRegister = state.formValid
                )
            }
            .launchIn(viewModelScope)

        state.email.textAsFlow()
            .onEach { email ->
                if (email == "")
                {
                    return@onEach
                }

                val emailResult = validateEmail(state.email.text.toString())
                state = state.copy(
                    emailError = if (emailResult.inValidEmail) "Invalid email provided" else "",
                    invalidEmail = emailResult.inValidEmail
                )

                state = state.copy(
                    canRegister = state.formValid
                )
            }
            .launchIn(viewModelScope)
        combine(state.password.textAsFlow(), state.repeatedPassword.textAsFlow()) { password, repeat ->
            if (password == "")
            {
                return@combine
            }

            val passwordResult = validatePassword(password.toString(), repeat.toString())
            state = state.copy(
                passwordError = if (passwordResult.invalidPassword) "Password must be at least 8 characters and include a number or symbol" else "",
                invalidUsername = passwordResult.invalidPassword,
                passwordEqualityError = if (passwordResult.isNotEqual) "Passwords do not match" else ""
            )

            state = state.copy(
                canRegister = state.formValid
            )
        }.launchIn(viewModelScope)
    }

    fun TextFieldState.textAsFlow() : Flow<CharSequence> = snapshotFlow { text }

    fun onAction(action: RegistrationAction) {
        when(action) {
            RegistrationAction.OnRegisterClick -> {
                if (!state.canRegister)
                {
                    return;
                }

                register()
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            state = state.copy(isRegistering = true)

            val result = authRepository.register(
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