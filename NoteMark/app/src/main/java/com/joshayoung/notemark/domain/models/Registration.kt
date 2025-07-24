package com.joshayoung.notemark.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Registration(
    val username: String,
    val email: String,
    val password: String
)