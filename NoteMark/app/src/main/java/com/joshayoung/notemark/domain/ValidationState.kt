package com.joshayoung.notemark.domain

data class ValidationState (
    val isNotEqual: Boolean = false,
    val inValidEmail: Boolean = false,
    val invalidUsername : Boolean = false,
    val error: String = ""
)