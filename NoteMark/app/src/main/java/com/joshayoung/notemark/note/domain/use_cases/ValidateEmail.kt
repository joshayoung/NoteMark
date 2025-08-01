package com.joshayoung.notemark.note.domain.use_cases

import com.joshayoung.notemark.core.domain.ValidationState

class ValidateEmail(
    private val patternValidator: PatternValidator
) {
    operator fun invoke(email: String) : ValidationState {
        val invalidEmail =  (!patternValidator.matches(email))

        return ValidationState(inValidEmail = invalidEmail)
    }
}