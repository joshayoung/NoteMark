package com.joshayoung.notemark.data

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val errors: List<String>? = null,
    val status: String? = null,
    val reason: String? = null
    val timestamp: String? = null,
    val error: String? = null,
    val path: String? = null
)