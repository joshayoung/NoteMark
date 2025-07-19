package com.joshayoung.notemark.domain

interface PatternValidator {
    fun matches(value: String) : Boolean
}