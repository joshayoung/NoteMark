package com.joshayoung.notemark.auth.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Registration(
    val username: String,
    val email: String,
    val password: String
)