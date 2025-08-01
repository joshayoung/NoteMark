package com.joshayoung.notemark.auth.domain.repository

import com.joshayoung.notemark.core.domain.models.Result

interface AuthRepository {
    suspend fun register(username: String, email: String, password: String): Result
    suspend fun login(email: String, password: String): Result
}