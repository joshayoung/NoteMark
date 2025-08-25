package com.joshayoung.notemark.auth.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class LogoutRequest(
    val refreshToken: String,
)