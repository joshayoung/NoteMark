package com.joshayoung.notemark.auth.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class RefreshToken(
    val refreshToken: String,
)
