package com.joshayoung.notemark.presentation.registration

import androidx.compose.foundation.text.input.TextFieldState

data class RegistrationState(
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState()
)