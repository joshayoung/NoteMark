package com.joshayoung.notemark.domain.use_cases

import com.joshayoung.notemark.domain.ValidationState

class ValidateUsername {
    operator fun invoke(username: String) : ValidationState {
        val invalidUsername = username.length < 3

        return ValidationState(invalidUsername = invalidUsername)
    }
}