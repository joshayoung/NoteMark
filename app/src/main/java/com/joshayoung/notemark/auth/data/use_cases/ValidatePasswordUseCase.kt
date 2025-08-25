package com.joshayoung.notemark.auth.data.use_cases

import com.joshayoung.notemark.core.domain.use_cases.ValidationState
import kotlin.Boolean

class ValidatePasswordUseCase {
    operator fun invoke(
        password: String,
        repeatPassword: String,
    ): ValidationState {
        val isNotEqual = password != repeatPassword
        val invalidPassword = password.length < 8 || !digitOrSpecialCharacter(password)

        return ValidationState(
            invalidPassword = invalidPassword || isNotEqual,
            isNotEqual = isNotEqual,
        )
    }

    private fun digitOrSpecialCharacter(password: String): Boolean {
        val regex = Regex("[0-9!@#\$%^&*(),.?\":{}|<>]")

        return regex.containsMatchIn(password)
    }
}