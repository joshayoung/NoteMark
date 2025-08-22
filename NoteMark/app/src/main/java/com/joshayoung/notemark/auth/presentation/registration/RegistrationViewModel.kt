package com.joshayoung.notemark.auth.presentation.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.navOptions
import com.joshayoung.notemark.auth.domain.repository.AuthRepository
import com.joshayoung.notemark.core.domain.util.Result
import com.joshayoung.notemark.core.navigation.Destination
import com.joshayoung.notemark.core.navigation.Navigator
import com.joshayoung.notemark.core.utils.textAsFlow
import com.joshayoung.notemark.note.domain.use_cases.ValidateEmail
import com.joshayoung.notemark.note.domain.use_cases.ValidatePassword
import com.joshayoung.notemark.note.domain.use_cases.ValidateUsername
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val authRepository: AuthRepository,
    private val validateUsername: ValidateUsername,
    private val validatePassword: ValidatePassword,
    private val validateEmail: ValidateEmail,
    private val navigator: Navigator,
) : ViewModel() {
    var state by mutableStateOf(RegistrationState())
        private set

    private val eventChannel = Channel<RegistrationEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        state.username
            .textAsFlow()
            .onEach { username ->
                if (username == "") {
                    return@onEach
                }

                val usernameResult = validateUsername(state.username.text.toString())
                state =
                    state.copy(
                        usernameError = if (usernameResult.invalidUsername) "Username must be at least 3 characters" else "",
                        invalidUsername = usernameResult.invalidUsername,
                    )

                state =
                    state.copy(
                        canRegister = state.formValid,
                    )
            }.launchIn(viewModelScope)

        state.email
            .textAsFlow()
            .onEach { email ->
                if (email == "") {
                    return@onEach
                }

                val emailResult = validateEmail(state.email.text.toString())
                state =
                    state.copy(
                        emailError = if (emailResult.inValidEmail) "Invalid email provided" else "",
                        invalidEmail = emailResult.inValidEmail,
                    )

                state =
                    state.copy(
                        canRegister = state.formValid,
                    )
            }.launchIn(viewModelScope)
        combine(state.password.textAsFlow(), state.repeatedPassword.textAsFlow()) { password, repeat ->
            if (password == "") {
                return@combine
            }

            val passwordResult = validatePassword(password.toString(), repeat.toString())
            state =
                state.copy(
                    passwordError = if (passwordResult.invalidPassword) "Password must be at least 8 characters and include a number or symbol" else "",
                    invalidUsername = passwordResult.invalidPassword,
                    passwordEqualityError = if (passwordResult.isNotEqual) "Passwords do not match" else "",
                )

            state =
                state.copy(
                    canRegister = state.formValid,
                )
        }.launchIn(viewModelScope)
    }

    fun onAction(action: RegistrationAction) {
        when (action) {
            RegistrationAction.OnRegisterClick -> {
                if (!state.canRegister) {
                    return
                }

                register()
            }

            RegistrationAction.AlreadyAccount -> {
                viewModelScope.launch {
                    navigator.navigate(
                        destination = Destination.Login,
                        navOptions = {
                            popUpTo(Destination.StartScreen)
                        },
                    )
                }
            }

            RegistrationAction.RegisterSuccess -> {
                viewModelScope.launch {
                    navigator.navigate(Destination.Login)
                }
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            state = state.copy(isRegistering = true)

            val result =
                authRepository.register(
                    username = state.username.text.toString(),
                    email = state.email.text.toString(),
                    password = state.password.text.toString(),
                )

            state = state.copy(isRegistering = false)

            when (result) {
                is Result.Error -> {
                    // TODO: Get the specific error:
                    eventChannel.send(RegistrationEvent.Error("Error Registering"))
                }
                is Result.Success -> {
                    eventChannel.send(RegistrationEvent.RegistrationSuccess)
                }
            }
        }
    }
}