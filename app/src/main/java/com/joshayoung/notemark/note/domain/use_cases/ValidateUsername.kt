package com.joshayoung.notemark.note.domain.use_cases

import com.joshayoung.notemark.core.domain.ValidationState

class ValidateUsername {
    operator fun invoke(username: String): ValidationState {
        val invalidUsername = username.length < 3 || username.length > 20

        return ValidationState(invalidUsername = invalidUsername)
    }
}