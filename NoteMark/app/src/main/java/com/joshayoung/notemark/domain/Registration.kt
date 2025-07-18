package com.joshayoung.notemark.domain

import kotlinx.serialization.Serializable

@Serializable
data class Registration(
    val username: String,
    val email: String,
    val password: String
)