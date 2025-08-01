package com.joshayoung.notemark.auth.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val username: String? = null
)