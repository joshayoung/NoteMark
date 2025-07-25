package com.joshayoung.notemark.data.use_cases

import android.util.Patterns
import com.joshayoung.notemark.domain.use_cases.PatternValidator

class EmailValidator : PatternValidator {
    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}