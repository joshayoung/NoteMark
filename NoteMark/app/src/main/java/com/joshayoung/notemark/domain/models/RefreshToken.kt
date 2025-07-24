package com.joshayoung.notemark.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class RefreshToken(
    val refreshToken: String
)
