package com.joshayoung.notemark.domain

import kotlinx.serialization.Serializable

@Serializable
data class RefreshToken(
    val refreshToken: String
)
