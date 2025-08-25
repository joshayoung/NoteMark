package com.joshayoung.notemark.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val errors: List<String>? = null,
    val status: String? = null,
    val reason: String? = null,
    val timestamp: String? = null,
    val error: String? = null,
    val path: String? = null,
)