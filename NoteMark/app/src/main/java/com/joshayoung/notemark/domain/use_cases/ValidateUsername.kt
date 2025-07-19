package com.joshayoung.notemark.domain.use_cases

import com.joshayoung.notemark.domain.ValidationState

class ValidateUsername {
    operator fun invoke(username: String) : ValidationState {
        if (username.length < 3)
        {
            return ValidationState(
                invalidUsername = true,
                error = "Your username must be at least 3 characters."
            )
        }

        return ValidationState(
            invalidUsername = false,
            error = ""
        )
    }
}