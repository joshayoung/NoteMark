package com.joshayoung.notemark.auth.domain.use_cases

interface PatternValidator {
    fun matches(value: String): Boolean
}