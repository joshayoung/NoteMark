package com.joshayoung.notemark.domain.use_cases

import androidx.compose.animation.core.infiniteRepeatable
import com.joshayoung.notemark.domain.use_cases.PatternValidator
import com.joshayoung.notemark.domain.ValidationState

class ValidateEmail(
    private val patternValidator: PatternValidator
) {
    operator fun invoke(email: String) : ValidationState {
        val invalidEmail =  (!patternValidator.matches(email))

        return ValidationState(inValidEmail = invalidEmail)
    }
}