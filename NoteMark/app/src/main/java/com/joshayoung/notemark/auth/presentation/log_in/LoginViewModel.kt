package com.joshayoung.notemark.auth.presentation.log_in

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joshayoung.notemark.auth.domain.repository.AuthRepository
import com.joshayoung.notemark.core.data.DataStorageImpl
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import com.joshayoung.notemark.note.domain.use_cases.ValidateEmail
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val validateEmail: ValidateEmail,
    private val dataStorageImpl: DataStorageImpl
) : ViewModel() {
    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        // TODO: Find a better way:
        combine(
            state.username.textAsFlow(),
            state.password.textAsFlow()
        ) { username, password ->
            state = state.copy(formFilled = filledWithValidEmail(username, password))
        }.launchIn(viewModelScope)
    }

    fun TextFieldState.textAsFlow() : Flow<CharSequence> = snapshotFlow { text }

    fun onAction(action: LoginAction) {
        when(action) {
            LoginAction.OnLoginClick -> {
                viewModelScope.launch {
                    state = state.copy(isLoggingIn = true)
                    viewModelScope.launch {
                        val result = authRepository.login(
                            state.username.text.toString(),
                            state.password.text.toString(),
                        )

                        if (!result.success) {
                            state = state.copy(isLoggingIn = false)
                            eventChannel.send(LoginEvent.Failure)
                        }

                        if (result.success) {
                            eventChannel.send(LoginEvent.Success)
                        }
                    }
                }
            }
        }
    }

    private fun filledWithValidEmail(username: CharSequence, password: CharSequence): Boolean {
        val bothFilled = username != "" && password != ""
        val validEmail = validateEmail.invoke(username.toString())

        return bothFilled && !validEmail.inValidEmail
    }
}


