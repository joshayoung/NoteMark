package com.joshayoung.notemark.note.domain.use_cases

interface PatternValidator {
    fun matches(value: String) : Boolean
}