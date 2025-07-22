package com.joshayoung.notemark.domain.use_cases

import com.joshayoung.notemark.domain.ValidationState
import kotlin.Boolean

class ValidatePassword {
    operator fun invoke(password: String, repeatPassword: String) : ValidationState {
        val isNotEqual = password != repeatPassword
        val invalidPassword = password.length < 8 || !digitOrSpecialCharacter(password)

        return ValidationState(
            isNotEqual = isNotEqual,
            invalidPassword = invalidPassword,
        )
    }

    private fun digitOrSpecialCharacter(password: String): Boolean {
        val regex = Regex("\\d|[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]")

        return regex.matches(password)
    }
}