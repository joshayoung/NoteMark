package com.joshayoung.notemark.presentation.registration

import androidx.compose.foundation.text.input.TextFieldState

data class RegistrationState(
    val username: TextFieldState = TextFieldState(),
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val repeatedPassword: TextFieldState = TextFieldState(),
    val isRegistering: Boolean = false,
    val canRegister: Boolean = false,
    val errors: List<String>? = null
)