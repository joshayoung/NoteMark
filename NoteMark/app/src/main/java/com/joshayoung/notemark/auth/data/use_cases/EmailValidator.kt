package com.joshayoung.notemark.auth.data.use_cases

import android.util.Patterns
import com.joshayoung.notemark.note.domain.use_cases.PatternValidator

class EmailValidator : PatternValidator {
    override fun matches(value: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(value).matches()
}