package com.joshayoung.notemark.auth.presentation.registration

import androidx.compose.foundation.text.input.TextFieldState

data class RegistrationState(
    val username: TextFieldState = TextFieldState(),
    val email: TextFieldState = TextFieldState(),
    val password: TextFieldState = TextFieldState(),
    val repeatedPassword: TextFieldState = TextFieldState(),
    val isRegistering: Boolean = false,
    val canRegister: Boolean = false,
    val errors: List<String>? = null,
    val usernameError: String = "",
    val emailError: String = "",
    val passwordError: String = "",
    val passwordEqualityError: String = "",
    val passwordsNotEqual: Boolean = false,

    val invalidPassword: Boolean = false,
    val invalidUsername: Boolean = false,
    val invalidEmail: Boolean = false
) {

    val formValid: Boolean
        get() = !invalidUsername && !invalidEmail && !invalidPassword

}