package com.joshayoung.notemark.domain.use_cases

import com.joshayoung.notemark.domain.ValidationState

class ValidatePassword {
    operator fun invoke(password: String, repeatPassword: String) : ValidationState {
        if (password != repeatPassword)
        {
            return ValidationState(
                isNotEqual = true,
                error = "Your passwords do not match."
            )
        }

        if (password.length < 8) {
            return ValidationState(
                isNotEqual = true,
                error = "Your password must be at least 8 characters long."
            )
        }

        if (!digitOrSpecialCharacter(password)) {
            return ValidationState(
                isNotEqual = true,
                error = "Your password must have either a digit or a special character."
            )
        }

        return ValidationState(isNotEqual = false)
    }

    private fun digitOrSpecialCharacter(password: String): Boolean {
        val regex = Regex("\\d|[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]")

        return regex.matches(password)
    }
}