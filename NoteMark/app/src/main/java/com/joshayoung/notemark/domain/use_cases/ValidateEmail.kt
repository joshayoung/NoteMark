package com.joshayoung.notemark.domain.use_cases

import com.joshayoung.notemark.domain.PatternValidator
import com.joshayoung.notemark.domain.ValidationState

class ValidateEmail(
    private val patternValidator: PatternValidator
) {
    operator fun invoke(email: String) : ValidationState {
        if (!patternValidator.matches(email)) {
            return ValidationState(
                inValidEmail = true,
                error = "Your email address is invalid",
            )
        }

        return ValidationState(inValidEmail = false)
    }
}