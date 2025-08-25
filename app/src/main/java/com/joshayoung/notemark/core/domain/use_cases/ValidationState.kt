package com.joshayoung.notemark.core.domain.use_cases

data class ValidationState(
    val isNotEqual: Boolean = false,
    val inValidEmail: Boolean = false,
    val invalidUsername: Boolean = false,
    val invalidPassword: Boolean = false,
)