package com.joshayoung.notemark.domain.models

typealias Error = com.joshayoung.notemark.data.Error

data class Result(
    var success: Boolean,
    var error: Error? = null
)