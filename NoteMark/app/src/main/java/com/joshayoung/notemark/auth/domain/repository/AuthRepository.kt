package com.joshayoung.notemark.auth.domain.repository

import com.joshayoung.notemark.core.domain.Result

interface AuthRepository {
    suspend fun register(username: String, email: String, password: String): com.joshayoung.notemark.core.domain.Result
    suspend fun login(email: String, password: String): com.joshayoung.notemark.core.domain.Result
}