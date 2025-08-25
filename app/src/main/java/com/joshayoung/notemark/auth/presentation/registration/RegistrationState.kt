package com.joshayoung.notemark.auth.presentation.registration

import androidx.compose.foundation.text.input.TextFieldState
import com.joshayoung.notemark.core.presentation.UiText

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
    val passwordError: UiText? = null,
    val passwordEqualityError: String = "",
    val passwordsNotEqual: Boolean = false,
    val invalidPassword: Boolean = true,
    val invalidUsername: Boolean = true,
    val invalidEmail: Boolean = true,
) {
    val formValid: Boolean
        get() = !invalidUsername && !invalidEmail && !invalidPassword
}