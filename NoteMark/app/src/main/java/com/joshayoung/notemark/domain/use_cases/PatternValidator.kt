package com.joshayoung.notemark.domain.use_cases

interface PatternValidator {
    fun matches(value: String) : Boolean
}