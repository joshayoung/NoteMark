package com.joshayoung.notemark.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Login(
    val email: String,
    val password: String
)