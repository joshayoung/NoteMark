package com.joshayoung.notemark.auth.data.use_cases

import com.joshayoung.notemark.core.domain.use_cases.ValidationState

class ValidateUsernameUseCase {
    operator fun invoke(username: String): ValidationState {
        val invalidUsername = username.length < 3 || username.length > 20

        return ValidationState(invalidUsername = invalidUsername)
    }
}