package com.joshayoung.notemark.presentation.log_in

import androidx.compose.foundation.text.input.TextFieldState

data class LoginState(
    val username: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val formFilled: Boolean = false,
    val isLoggingIn: Boolean = false
)