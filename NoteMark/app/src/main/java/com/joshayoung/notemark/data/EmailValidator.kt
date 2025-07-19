package com.joshayoung.notemark.data

import android.util.Patterns
import com.joshayoung.notemark.domain.PatternValidator

class EmailValidator : PatternValidator {
    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}