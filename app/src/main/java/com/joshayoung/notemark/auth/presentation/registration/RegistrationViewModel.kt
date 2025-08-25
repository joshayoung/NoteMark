package com.joshayoung.notemark.auth.presentation.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.R
import com.joshayoung.notemark.auth.data.use_cases.ValidateEmail
import com.joshayoung.notemark.auth.data.use_cases.ValidatePassword
import com.joshayoung.notemark.auth.data.use_cases.ValidateUsername
import com.joshayoung.notemark.auth.domain.repository.AuthRepository
import com.joshayoung.notemark.core.domain.util.Result
import com.joshayoung.notemark.core.navigation.Destination
import com.joshayoung.notemark.core.navigation.Navigator
import com.joshayoung.notemark.core.utils.textAsFlow
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
                        usernameError = if (usernameResult.invalidUsername) "Username must be between 3 and 20 characters" else "",
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

            // TODO: Is this right?
            var registrationError = R.string.password_length_and_chars.toString()
            state =
                state.copy(
                    passwordError = if (passwordResult.invalidPassword) registrationError else "",
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
                        destination = Destination.LoginScreen,
                        navOptions = {
                            popUpTo(Destination.StartScreen)
                        },
                    )
                }
            }

            RegistrationAction.RegisterSuccess -> {
                viewModelScope.launch {
                    navigator.navigate(Destination.LoginScreen)
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